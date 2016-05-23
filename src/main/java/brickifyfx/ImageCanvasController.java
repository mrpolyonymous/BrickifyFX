package brickifyfx;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import brickifyfx.core.BricksAndColors;
import brickifyfx.quantisation.QuantisationMethod;

public class ImageCanvasController extends BasePaneController {
	@FXML private AnchorPane originalImagePane;

	private CropDrawMode cropDrawMode;
	private boolean cropMode;
	private double canvasCropOriginX, canvasCropOriginY, canvasCropToX, canvasCropToY;
	private Rectangle2D originalImageCropRectangle;
	private Point2D dragOrigin;
	private Point2D dragStartCropOrigin;
	private Point2D dragStartCropTo;

	private double scaleFactor, scaledOriginalImageWidth, scaledOriginalImageHeight;
	private Canvas originalCanvas;

	private CropPaneController cropPaneController;

	@FXML private TabPane imageTabPane;
	@FXML private Tab originalImageTab;

	private Map<RenderedMosaicKey, MosaicImageController> mosaicTabControllers = new HashMap<>();
	private int tabCount;

	private static class RenderedMosaicKey {
		private BricksAndColors bricksAndColors;
		private QuantisationMethod quantisationMethod;
		private boolean threeDEffect;
		private double imageWidth;
		private double imageHeight;
		public RenderedMosaicKey(BricksAndColors bricksAndColors,
				QuantisationMethod quantisationMethod,
				boolean threeDEffect,
				double imageWidth,
				double imageHeight) {
			this.bricksAndColors = bricksAndColors;
			this.quantisationMethod = quantisationMethod;
			this.threeDEffect = threeDEffect;
			this.imageWidth = imageWidth;
			this.imageHeight = imageHeight;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((bricksAndColors == null) ? 0 : bricksAndColors.hashCode());
			long temp;
			temp = Double.doubleToLongBits(imageHeight);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(imageWidth);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((quantisationMethod == null) ? 0 : quantisationMethod.hashCode());
			result = prime * result + (threeDEffect ? 1231 : 1237);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RenderedMosaicKey other = (RenderedMosaicKey) obj;
			if (bricksAndColors == null) {
				if (other.bricksAndColors != null)
					return false;
			} else if (!bricksAndColors.equals(other.bricksAndColors))
				return false;
			if (Double.doubleToLongBits(imageHeight) != Double.doubleToLongBits(other.imageHeight))
				return false;
			if (Double.doubleToLongBits(imageWidth) != Double.doubleToLongBits(other.imageWidth))
				return false;
			if (quantisationMethod != other.quantisationMethod)
				return false;
			if (threeDEffect != other.threeDEffect)
				return false;
			return true;
		}

	}


	static enum CropDrawMode {
		DRAW_ORIGINAL,
		RESIZE_TOP,
		RESIZE_TOP_RIGHT,
		RESIZE_RIGHT,
		RESIZE_BOTTOM_RIGHT,
		RESIZE_BOTTOM,
		RESIZE_BOTTOM_LEFT,
		RESIZE_LEFT,
		RESIZE_TOP_LEFT,
		MOVE_IMAGE,
		DO_NOTHING;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);

		tabCount = 1;
		imageTabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
		ChangeListener<Number> originalImageSizeChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateOriginalCanvas();
				if (originalImageCropRectangle != null) {
					canvasCropOriginX = originalImageCropRectangle.getMinX() * scaleFactor;
					canvasCropOriginY = originalImageCropRectangle.getMinY() * scaleFactor;
					canvasCropToX = canvasCropOriginX + originalImageCropRectangle.getWidth() * scaleFactor;
					canvasCropToY = canvasCropOriginY + originalImageCropRectangle.getHeight() * scaleFactor;
					Rectangle2D canvasCropRectangle = new Rectangle2D(
							Math.floor(canvasCropOriginX + 0.5),
							Math.floor(canvasCropOriginY + 0.5),
							Math.floor(canvasCropToX - canvasCropOriginX + 0.5),
							Math.floor(canvasCropToY - canvasCropOriginY + 0.5)
							);
					drawCropRectangle(canvasCropRectangle);
				}
			}
		};
		originalImagePane.heightProperty().addListener(originalImageSizeChangeListener);
		originalImagePane.widthProperty().addListener(originalImageSizeChangeListener);

	}

	@Override
	public void setMainController(BrickifyFXController mainController) {
		super.setMainController(mainController);

		createOriginalCanvas();
	}



	@Override
	public void imageLoaded(Image newImage) {
		resetCrop();
		createOriginalCanvas();

		mosaicTabControllers.clear();
		while (imageTabPane.getTabs().size() > 1) {
			imageTabPane.getTabs().remove(imageTabPane.getTabs().size() - 1);
		}
		tabCount = 1;
	}

	public void setCropPaneController(CropPaneController cropPaneController_) {
		this.cropPaneController = cropPaneController_;
		this.cropPaneController.getCropRatioChoice().valueProperty().addListener(new ChangeListener<CropRatio>() {

			@Override
			public void changed(ObservableValue<? extends CropRatio> observable, CropRatio oldValue, CropRatio newValue) {
				if (newValue == null) {
					return;
				}
				if (!cropMode) {
					cropMode = true;
					cropPaneController.updateCropModeStyles(cropMode);
				}
				if (!newValue.hasRatio()) {
					return;
				}
				if (originalImageCropRectangle != null) {
					double newCropRectangleHeight = Math.floor(newValue.convertWidthToHeight(originalImageCropRectangle.getWidth()));
					canvasCropToY = canvasCropOriginY + Math.floor(newCropRectangleHeight * scaleFactor);
					if (canvasCropToY > scaledOriginalImageHeight) {
						canvasCropToY = scaledOriginalImageHeight;
						double newWidth = newValue.convertHeightToWidth(canvasCropToY-canvasCropOriginY);
						canvasCropToX = canvasCropOriginX + newWidth;
					}

					originalImageCropRectangle = new Rectangle2D(originalImageCropRectangle.getMinX(),
							originalImageCropRectangle.getMinY(),
							(canvasCropToX - canvasCropOriginX) / scaleFactor,
							newCropRectangleHeight);

					Rectangle2D canvasCropRectangle = new Rectangle2D(
							canvasCropOriginX,
							canvasCropOriginY,
							canvasCropToX - canvasCropOriginX,
							canvasCropToY - canvasCropOriginY
							);

					updateOriginalCanvas();
					drawCropRectangle(canvasCropRectangle);

					cropPaneController.updateCropTextFields();
				}

			}

		});


	}

	@FXML
	protected void handleImageMousePress(MouseEvent mouseEvent) {
		if (cropMode && mouseEvent.isPrimaryButtonDown()) {
			double mouseX = mouseEvent.getX();
			double mouseY = mouseEvent.getY();
			if (originalImageCropRectangle == null) {
				canvasCropOriginX = Math.floor(mouseX);
				canvasCropToX = canvasCropOriginX;
				canvasCropOriginY = Math.floor(mouseY);
				canvasCropToY = canvasCropOriginY;
//				Point2D cropOrigin = new Point2D(canvasCropOriginX, canvasCropOriginY);
//				System.out.println("Crop origin (canvas coordinates): " + cropOrigin);
				cropDrawMode = CropDrawMode.DRAW_ORIGINAL;
			} else if (isMouseNearTopLeftOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_TOP_LEFT;
			} else if (isMouseNearTopRightOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_TOP_RIGHT;
			} else if (isMouseNearBottomLeftOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_BOTTOM_LEFT;
			} else if (isMouseNearBottomRightOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_BOTTOM_RIGHT;
			} else if (isMouseNearTopOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_TOP;
			} else if (isMouseNearRightOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_RIGHT;
			} else if (isMouseNearBottomOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_BOTTOM;
			} else if (isMouseNearLeftOfCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.RESIZE_LEFT;
			} else if (isMouseOverCropRectangle(mouseX, mouseY)) {
				cropDrawMode = CropDrawMode.MOVE_IMAGE;
				dragOrigin = new Point2D(mouseX, mouseY);
				dragStartCropOrigin = new Point2D(canvasCropOriginX, canvasCropOriginY);
				dragStartCropTo = new Point2D(canvasCropToX, canvasCropToY);
			} else {
				cropDrawMode = CropDrawMode.DO_NOTHING;
			}
		}

	}

	private static final double MIN_CROP_DIMENSION = 10.0;

	@FXML
	protected void handleImageDrag(MouseEvent mouseEvent) {
		if (cropMode) {
			double mouseX = mouseEvent.getX();
			double mouseY = mouseEvent.getY();
			CropRatio cropRatio = cropPaneController.getCropRatioChoice().getValue();
			Rectangle2D rectangleToDraw = null;
			boolean drawRectangle = true;

			switch (cropDrawMode) {
			case DRAW_ORIGINAL:
				updateCropExtents(mouseEvent);
				double drawOriginX = canvasCropOriginX;
				double drawOriginY = canvasCropOriginY;
				double canvasCropWidth = canvasCropToX - canvasCropOriginX;
				if (canvasCropWidth < 0) {
					canvasCropWidth = -canvasCropWidth;
					drawOriginX = canvasCropToX;
				}
				if (drawOriginX + canvasCropWidth > scaledOriginalImageWidth) {
					canvasCropWidth = scaledOriginalImageWidth - drawOriginX;
				}

				double canvasCropHeight = canvasCropToY - canvasCropOriginY;
				if (canvasCropHeight < 0) {
					canvasCropHeight = -canvasCropHeight;
					drawOriginY = canvasCropToY;
				}
				if (drawOriginY + canvasCropHeight > scaledOriginalImageHeight) {
					canvasCropHeight = scaledOriginalImageHeight - drawOriginY;
				}

				updateOriginalCanvas();
				rectangleToDraw = new Rectangle2D(drawOriginX, drawOriginY, canvasCropWidth, canvasCropHeight);

				cropPaneController.updateCropTextFields();
				break;

			case MOVE_IMAGE:
				double xDiff = mouseX - dragOrigin.getX();
				double yDiff = mouseY - dragOrigin.getY();
				if (xDiff < 0) {
					if (dragStartCropOrigin.getX() + xDiff < 0) {
						xDiff = -dragStartCropOrigin.getX();
					}
				} else {
					if (dragStartCropTo.getX() + xDiff > scaledOriginalImageWidth) {
						xDiff = scaledOriginalImageWidth - dragStartCropTo.getX();
					}
				}
				if (yDiff < 0) {
					if (dragStartCropOrigin.getY() + yDiff < 0) {
						yDiff = -dragStartCropOrigin.getY();
					}
				} else {
					if (dragStartCropTo.getY() + yDiff > scaledOriginalImageHeight) {
						yDiff = scaledOriginalImageHeight - dragStartCropTo.getY();
					}
				}
				canvasCropOriginX = dragStartCropOrigin.getX() + xDiff;
				canvasCropToX = dragStartCropTo.getX() + xDiff;
				canvasCropOriginY = dragStartCropOrigin.getY() + yDiff;
				canvasCropToY = dragStartCropTo.getY() + yDiff;
				break;

			case RESIZE_TOP:
				canvasCropOriginY = MathUtils.clamp(mouseY, 0, canvasCropToY - MIN_CROP_DIMENSION);

				if (cropRatio.hasRatio()) {
					double newWidth = cropRatio.convertHeightToWidth(canvasCropToY - canvasCropOriginY);
					double widthDiff = newWidth - (canvasCropToX - canvasCropOriginX);
					canvasCropOriginX -= widthDiff / 2;
					canvasCropToX += widthDiff / 2;

					double overflow = MathUtils.max(0, -canvasCropOriginX, canvasCropToX - scaledOriginalImageWidth);
					if (overflow > 0) {
						canvasCropOriginX += overflow;
						canvasCropToX -= overflow;
						newWidth = canvasCropToX - canvasCropOriginX;
						double newHeight = cropRatio.convertWidthToHeight(newWidth);
						canvasCropOriginY = canvasCropToY - newHeight;
					}
				}

				break;

			case RESIZE_BOTTOM:
				canvasCropToY = MathUtils.clamp(mouseY, canvasCropOriginY + MIN_CROP_DIMENSION, scaledOriginalImageHeight);

				if (cropRatio.hasRatio()) {
					double newWidth = cropRatio.convertHeightToWidth(canvasCropToY - canvasCropOriginY);
					double widthDiff = newWidth - (canvasCropToX - canvasCropOriginX);
					canvasCropOriginX -= widthDiff / 2;
					canvasCropToX += widthDiff / 2;

					double overflow = MathUtils.max(0, -canvasCropOriginX, canvasCropToX - scaledOriginalImageWidth);
					if (overflow > 0) {
						canvasCropOriginX += overflow;
						canvasCropToX -= overflow;
						newWidth = canvasCropToX - canvasCropOriginX;
						double newHeight = cropRatio.convertWidthToHeight(newWidth);
						canvasCropToY = canvasCropOriginY + newHeight;
					}
				}

				break;

			case RESIZE_LEFT:
				canvasCropOriginX = MathUtils.clamp(mouseX, 0, canvasCropToX - MIN_CROP_DIMENSION);

				if (cropRatio.hasRatio()) {
					double newHeight = cropRatio.convertWidthToHeight(canvasCropToX - canvasCropOriginX);
					double heightDiff = newHeight - (canvasCropToY - canvasCropOriginY);
					canvasCropOriginY -= heightDiff / 2;
					canvasCropToY += heightDiff / 2;

					double overflow = MathUtils.max(0, -canvasCropOriginY, canvasCropToY - scaledOriginalImageHeight);
					if (overflow > 0) {
						canvasCropOriginY += overflow;
						canvasCropToY -= overflow;
						newHeight = canvasCropToY - canvasCropOriginY;
						double newWidth = cropRatio.convertWidthToHeight(newHeight);
						canvasCropOriginX = canvasCropToX - newWidth;
					}
				}

				break;

			case RESIZE_RIGHT:
				canvasCropToX = MathUtils.clamp(mouseX, canvasCropOriginX + MIN_CROP_DIMENSION, scaledOriginalImageWidth);

				if (cropRatio.hasRatio()) {
					double newHeight = cropRatio.convertWidthToHeight(canvasCropToX - canvasCropOriginX);
					double heightDiff = newHeight - (canvasCropToY - canvasCropOriginY);
					canvasCropOriginY -= heightDiff / 2;
					canvasCropToY += heightDiff / 2;

					double overflow = MathUtils.max(0, -canvasCropOriginY, canvasCropToY - scaledOriginalImageHeight);
					if (overflow > 0) {
						canvasCropOriginY += overflow;
						canvasCropToY -= overflow;
						newHeight = canvasCropToY - canvasCropOriginY;
						double newWidth = cropRatio.convertWidthToHeight(newHeight);
						canvasCropToX = canvasCropOriginX + newWidth;
					}
				}

				break;

			case RESIZE_BOTTOM_RIGHT:
				mouseX = MathUtils.clamp(mouseX, canvasCropOriginX + MIN_CROP_DIMENSION, scaledOriginalImageWidth);
				mouseY = MathUtils.clamp(mouseY, canvasCropOriginY + MIN_CROP_DIMENSION, scaledOriginalImageHeight);

				if (cropRatio.hasRatio()) {
					canvasCropToX = mouseX;
					double newHeight = cropRatio.convertWidthToHeight(canvasCropToX - canvasCropOriginX);
					if (canvasCropOriginY + newHeight > scaledOriginalImageHeight) {
						newHeight = scaledOriginalImageHeight - canvasCropOriginY;
						canvasCropToY = scaledOriginalImageHeight;
						double newWidth = cropRatio.convertHeightToWidth(newHeight);
						canvasCropToX = canvasCropOriginX + newWidth;
					} else {
						canvasCropToY = canvasCropOriginY + newHeight;
					}
				} else {
					canvasCropToX = mouseX;
					canvasCropToY = mouseY;
				}

				break;

			case RESIZE_BOTTOM_LEFT:
				mouseX = MathUtils.clamp(mouseX, 0, canvasCropToX - MIN_CROP_DIMENSION);
				mouseY = MathUtils.clamp(mouseY, canvasCropOriginY + MIN_CROP_DIMENSION, scaledOriginalImageHeight);

				if (cropRatio.hasRatio()) {
					canvasCropOriginX = mouseX;
					double newHeight = cropRatio.convertWidthToHeight(canvasCropToX - canvasCropOriginX);
					if (canvasCropOriginY + newHeight > scaledOriginalImageHeight) {
						newHeight = scaledOriginalImageHeight - canvasCropOriginY;
						canvasCropToY = scaledOriginalImageHeight;
						double newWidth = cropRatio.convertHeightToWidth(newHeight);
						canvasCropOriginX = canvasCropToX - newWidth;
					} else {
						canvasCropToY = canvasCropOriginY + newHeight;
					}
				} else {
					canvasCropOriginX = mouseX;
					canvasCropToY = mouseY;
				}

				break;

			case RESIZE_TOP_LEFT:
				mouseX = MathUtils.clamp(mouseX, 0, canvasCropToX - MIN_CROP_DIMENSION);
				mouseY = MathUtils.clamp(mouseY, 0, canvasCropToY - MIN_CROP_DIMENSION);

				if (cropRatio.hasRatio()) {
					canvasCropOriginX = mouseX;
					double newHeight = cropRatio.convertWidthToHeight(canvasCropToX - canvasCropOriginX);
					if (newHeight > canvasCropToY) {
						newHeight = canvasCropToY;
						canvasCropOriginY = 0;
						double newWidth = cropRatio.convertHeightToWidth(newHeight);
						canvasCropOriginX = canvasCropToX - newWidth;
					} else {
						canvasCropOriginY = canvasCropToY - newHeight;
					}

				} else {
					canvasCropOriginX = mouseX;
					canvasCropOriginY = mouseY;
				}

				break;

			case RESIZE_TOP_RIGHT:
				mouseX = MathUtils.clamp(mouseX, canvasCropOriginX + MIN_CROP_DIMENSION, scaledOriginalImageWidth);
				mouseY = MathUtils.clamp(mouseY, 0, canvasCropToY - MIN_CROP_DIMENSION);

				if (cropRatio.hasRatio()) {
					canvasCropToX = mouseX;
					double newHeight = cropRatio.convertWidthToHeight(canvasCropToX - canvasCropOriginX);
					if (newHeight > canvasCropToY) {
						newHeight = canvasCropToY;
						canvasCropOriginY = 0;
						double newWidth = cropRatio.convertHeightToWidth(newHeight);
						canvasCropToX = canvasCropOriginX + newWidth;
					} else {
						canvasCropOriginY = canvasCropToY - newHeight;
					}

				} else {
					canvasCropToX = mouseX;
					canvasCropOriginY = mouseY;
				}

				break;

			case DO_NOTHING:
				drawRectangle = false;
				break;
			default:
				drawRectangle = false;
				break;
			}

			if (drawRectangle) {
				if (rectangleToDraw == null) {
					rectangleToDraw = new Rectangle2D(canvasCropOriginX, canvasCropOriginY, canvasCropToX - canvasCropOriginX, canvasCropToY - canvasCropOriginY);
				}
				updateOriginalCanvas();
				drawCropRectangle(rectangleToDraw);

				cropPaneController.updateCropTextFields();
			}
		}
	}

	@FXML
	protected void handleImageMouseMove(MouseEvent mouseEvent) {
		if (originalImageCropRectangle == null) {
			return;
		} else {
			double mouseX = mouseEvent.getX();
			double mouseY = mouseEvent.getY();
			if (isMouseNearTopLeftOfCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.NW_RESIZE);
			} else if (isMouseNearTopRightOfCropRectangle(mouseX, mouseY)) {
					originalCanvas.setCursor(Cursor.NE_RESIZE);
			} else if (isMouseNearBottomLeftOfCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.SW_RESIZE);
			} else if (isMouseNearBottomRightOfCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.SE_RESIZE);
			} else if (isMouseNearTopOfCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.N_RESIZE);
			} else if (isMouseNearRightOfCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.E_RESIZE);
			} else if (isMouseNearBottomOfCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.S_RESIZE);
			} else if (isMouseNearLeftOfCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.W_RESIZE);
			} else if (isMouseOverCropRectangle(mouseX, mouseY)) {
				originalCanvas.setCursor(Cursor.HAND);
			} else {
				originalCanvas.setCursor(Cursor.DEFAULT);
			}
		}
	}

	private boolean isMouseNearTopLeftOfCropRectangle(double mouseX, double mouseY) {
		return isMouseNearPoint(mouseX, mouseY, canvasCropOriginX, canvasCropOriginY);
	}

	private boolean isMouseNearTopRightOfCropRectangle(double mouseX, double mouseY) {
		return isMouseNearPoint(mouseX, mouseY, canvasCropToX, canvasCropOriginY);
	}

	private boolean isMouseNearBottomLeftOfCropRectangle(double mouseX, double mouseY) {
		return isMouseNearPoint(mouseX, mouseY, canvasCropOriginX, canvasCropToY);
	}

	private boolean isMouseNearBottomRightOfCropRectangle(double mouseX, double mouseY) {
		return isMouseNearPoint(mouseX, mouseY, canvasCropToX, canvasCropToY);
	}

	private boolean isMouseNearTopOfCropRectangle(double mouseX, double mouseY) {
		return mouseX > canvasCropOriginX && mouseX < canvasCropToX && isMouseCoordNear(mouseY, canvasCropOriginY);
	}

	private boolean isMouseNearRightOfCropRectangle(double mouseX, double mouseY) {
		return mouseY > canvasCropOriginY && mouseY < canvasCropToY && isMouseCoordNear(mouseX, canvasCropToX);
	}

	private boolean isMouseNearBottomOfCropRectangle(double mouseX, double mouseY) {
		return mouseX > canvasCropOriginX && mouseX < canvasCropToX && isMouseCoordNear(mouseY, canvasCropToY);
	}

	private boolean isMouseNearLeftOfCropRectangle(double mouseX, double mouseY) {
		return mouseY > canvasCropOriginY && mouseY < canvasCropToY && isMouseCoordNear(mouseX, canvasCropOriginX);
	}

	private boolean isMouseOverCropRectangle(double mouseX, double mouseY) {
		return mouseX > canvasCropOriginX && mouseX < canvasCropToX &&
				mouseY > canvasCropOriginY && mouseY < canvasCropToY;
	}

	private static boolean isMouseNearPoint(double mouseX, double mouseY, double pointX, double pointY) {
		return Math.abs(mouseX - pointX) < 5 && Math.abs(mouseY - pointY) < 5;
	}

	private static boolean isMouseCoordNear(double mouseCoord, double pointCoord) {
		return Math.abs(mouseCoord - pointCoord) < 5;
	}

	@FXML
	protected void handleImageMouseRelease(MouseEvent mouseEvent) {
		if (cropMode) {
			switch (cropDrawMode) {
			case DRAW_ORIGINAL:
				updateCropExtents(mouseEvent);

				// Force crop origin to be the top-left of the drawn crop rectangle
				if (canvasCropToX < canvasCropOriginX) {
					double tempX = canvasCropToX;
					canvasCropToX = canvasCropOriginX;
					canvasCropOriginX = tempX;
				}
				if (canvasCropToY < canvasCropOriginY) {
					double tempY = canvasCropToY;
					canvasCropToY = canvasCropOriginY;
					canvasCropOriginY = tempY;
				}

				break;
			case MOVE_IMAGE:
				break;
			case RESIZE_BOTTOM:
				break;
			case RESIZE_BOTTOM_LEFT:
				break;
			case RESIZE_BOTTOM_RIGHT:
				break;
			case RESIZE_LEFT:
				break;
			case RESIZE_RIGHT:
				break;
			case RESIZE_TOP:
				break;
			case RESIZE_TOP_LEFT:
				break;
			case RESIZE_TOP_RIGHT:
				break;
			case DO_NOTHING:
				break;
			default:
				break;
			}

			// we have the crop coordinates relative to the canvas, now translate them to image coordinates
			Rectangle2D canvasCropRectangle = new Rectangle2D(canvasCropOriginX, canvasCropOriginY, canvasCropToX-canvasCropOriginX, canvasCropToY-canvasCropOriginY);
			System.out.println("Crop rectangle (canvas coordinates): " + canvasCropRectangle);

			originalImageCropRectangle = new Rectangle2D(Math.floor(canvasCropOriginX/scaleFactor + 0.5),
					Math.floor(canvasCropOriginY/scaleFactor + 0.5),
					Math.floor((canvasCropToX-canvasCropOriginX)/scaleFactor + 0.5),
					Math.floor((canvasCropToY-canvasCropOriginY)/scaleFactor + 0.5));
			System.out.println("Crop rectangle (image coordinates): " + originalImageCropRectangle);
			cropPaneController.updateCropTextFields();
		}
		cropDrawMode = CropDrawMode.DO_NOTHING;

	}

	private void updateCropExtents(MouseEvent mouseEvent) {
		// Make sure mouse coordinates aren't outside of the original image
		double mouseX = MathUtils.clamp(mouseEvent.getX(), 0, scaledOriginalImageWidth);
		double mouseY = MathUtils.clamp(mouseEvent.getY(), 0, scaledOriginalImageHeight);

		// Re-adjust the height of the crop window to maintain the crop aspect ratio
		CropRatio cropRatio = cropPaneController.getCropRatioChoice().getValue();
		double cropWidth = Math.abs(mouseX - canvasCropOriginX);
		if (cropRatio.hasRatio()) {
			double desiredCropHeight = cropRatio.convertWidthToHeight(cropWidth);
			if (mouseY >= canvasCropOriginY) {
				mouseY = canvasCropOriginY + desiredCropHeight;
			} else {
				mouseY = canvasCropOriginY - desiredCropHeight;
			}

			// Make sure the scaled crop rectangle doesn't go outside of the original image
			if (mouseY > scaledOriginalImageHeight) {
				mouseY = scaledOriginalImageHeight;
				// need to re-adjust mouseX to match aspect ratio
				double cropHeight = mouseY - canvasCropOriginY;
				double desiredCropWidth = cropRatio.convertHeightToWidth(cropHeight);
				if (mouseX >= canvasCropOriginX) {
					mouseX = canvasCropOriginX + desiredCropWidth;
				} else {
					mouseX = canvasCropOriginX - desiredCropWidth;
				}
			} else if (mouseY < 0) {
				mouseY = 0;
				// need to re-adjust mouseX to match aspect ratio
				double cropHeight = canvasCropOriginY;
				double desiredCropWidth = cropRatio.convertHeightToWidth(cropHeight);
				if (mouseX >= canvasCropOriginX) {
					mouseX = canvasCropOriginX + desiredCropWidth;
				} else {
					mouseX = canvasCropOriginX - desiredCropWidth;
				}
			}

		}
		canvasCropToX = mouseX;
		canvasCropToY = mouseY;
	}


	/**
	 * Draw the crop rectangle overlay
	 * @param cropRectangle
	 * 	The crop rectangle, in canvas coordinates
	 */
	private void drawCropRectangle(Rectangle2D cropRectangle) {
		// Draw 4 rectangles to partially obscure the cropped-out regions of the image
		GraphicsContext graphicsContext = originalCanvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.rgb(0, 0, 0, 0.25));
		graphicsContext.fillRect(0, 0, scaledOriginalImageWidth, cropRectangle.getMinY());
		graphicsContext.fillRect(0, cropRectangle.getMinY(), cropRectangle.getMinX(), cropRectangle.getHeight());
		graphicsContext.fillRect(cropRectangle.getMaxX(), cropRectangle.getMinY(), scaledOriginalImageWidth - (cropRectangle.getMaxX()), cropRectangle.getHeight());
		graphicsContext.fillRect(0, cropRectangle.getMaxY(), scaledOriginalImageWidth, scaledOriginalImageHeight - (cropRectangle.getMaxY()));

		// complaint: where is it documented that you have to offset by 0.5 to get a pixel-sharp line?
		graphicsContext.setStroke(Color.BLACK);
		graphicsContext.setLineWidth(1.0);
		graphicsContext.strokeRect(cropRectangle.getMinX() + 0.5, cropRectangle.getMinY() + 0.5, cropRectangle.getWidth(), cropRectangle.getHeight());

		graphicsContext.setStroke(Color.color(0.95, 0.95, 0.95, 0.75));
		graphicsContext.strokeRect(cropRectangle.getMinX() - 0.5, cropRectangle.getMinY() - 0.5, cropRectangle.getWidth() + 2, cropRectangle.getHeight() + 2);
	}

	@FXML
	protected void handleImageAreaResize() {

	}

	protected void handleClearCrop() {
		resetCrop();
		updateOriginalCanvas();
	}

	void createOriginalCanvas() {
		Image originalImage = mainController.getOriginalImage();
		originalCanvas = new Canvas(originalImage.getWidth(), originalImage.getHeight());
		originalCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleImageMousePress(event);
			}
		});
		originalCanvas.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleImageMouseRelease(event);
			}
		});
		originalCanvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleImageDrag(event);
			}
		});
		originalCanvas.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleImageMouseMove(event);
			}
		});

		updateOriginalCanvas();
		originalImagePane.getChildren().clear();
		originalImagePane.getChildren().add(originalCanvas);

	}

	private void updateOriginalCanvas() {
		Image originalImage = mainController.getOriginalImage();
		GraphicsContext canvasGraphics = originalCanvas.getGraphicsContext2D();
		canvasGraphics.setFill(Color.WHITE);
		canvasGraphics.fillRect(0, 0, originalCanvas.getWidth(), originalCanvas.getHeight());

		double scaleX = (originalImagePane.getWidth() - 20.0)/ originalImage.getWidth();
		double scaleY = (originalImagePane.getHeight() - 20.0) / originalImage.getHeight();
		scaleFactor = MathUtils.min(1.0, scaleX, scaleY);

		scaledOriginalImageWidth = Math.floor(originalImage.getWidth() * scaleFactor);
		scaledOriginalImageHeight = Math.floor(originalImage.getHeight() * scaleFactor);

		canvasGraphics.drawImage(originalImage, 0, 0, scaledOriginalImageWidth, scaledOriginalImageHeight);

		double translateX = 10.0 + Math.floor((originalImagePane.getWidth() - 20.0 - scaledOriginalImageWidth) * 0.5);
		double translateY = 10.0 + Math.floor((originalImagePane.getHeight() - 20.0 - scaledOriginalImageHeight) * 0.5);
		originalCanvas.setTranslateX(translateX);
		originalCanvas.setTranslateY(translateY);

	}

	void resetCrop() {
		originalImageCropRectangle = null;

		canvasCropOriginX = canvasCropOriginY = canvasCropToX = canvasCropToY = 0;
	}

	public Rectangle2D getOriginalImageCropRectangle() {
		return originalImageCropRectangle;
	}

	void switchCropMode() {
		cropMode = !cropMode;
		cropPaneController.updateCropModeStyles(cropMode);
	}

	public double getCanvasCropToX() {
		return canvasCropToX;
	}

	public double getCanvasCropOriginX() {
		return canvasCropOriginX;
	}

	public double getCanvasCropToY() {
		return canvasCropToY;
	}

	public double getCanvasCropOriginY() {
		return canvasCropOriginY;
	}

	public double getScaleFactor() {
		return scaleFactor;
	}

	public Canvas getOriginalCanvas() {
		return originalCanvas;
	}

	@Override
	public void mosaicRendered(BricksAndColors bricksAndColors, QuantisationMethod quantisationMethod, boolean threeDEffect, Image mosaicImage) {
		// TODO - this should factor in crop origin, too.
		RenderedMosaicKey key = new RenderedMosaicKey(bricksAndColors, quantisationMethod, threeDEffect, mosaicImage.getWidth(), mosaicImage.getHeight());
		MosaicImageController mosaicImageController = mosaicTabControllers.get(key);
		if (mosaicImageController == null) {
			mosaicImageController = new MosaicImageController(this, mosaicImage, "Mosaic " + tabCount++);
			mosaicTabControllers.put(key, mosaicImageController);
			imageTabPane.getTabs().add(mosaicImageController.getTab());
		} else {
			mosaicImageController.resetImage(mosaicImage);
		}
		imageTabPane.getSelectionModel().select(mosaicImageController.getTab());
	}

	void tabClosed(MosaicImageController mosaicImageController) {
		// There is probably a better way to make this association using existing properties in Tab and TabPane classes.

		for (Map.Entry<RenderedMosaicKey, MosaicImageController> mapEntry : mosaicTabControllers.entrySet()) {
			if (mapEntry.getValue() == mosaicImageController) {
				mosaicTabControllers.remove(mapEntry.getKey());
				return;
			}
		}
	}


}
