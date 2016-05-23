package brickifyfx;

import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller that creates and manages individual tabs for rendered mosaic images
 */
public class MosaicImageController {
	private Tab mosaicImageTab;
	private AnchorPane mosaicImageAnchorPane;
	private ImageView mosaicImageView;
	private Point2D mosaicImageDragOrigin;
	private double originalTranslateX, originalTranslateY;
	private boolean wasDragged;

	private MosaicImageZoomState mosaicImageZoomState;

	private ImageCanvasController parentController;
	
	private enum MosaicImageZoomState {
		NO_ZOOM, ONE_TO_ONE, OVERLAY_ORIGINAL;
	}
	
	public MosaicImageController(ImageCanvasController imageCanvasController, Image mosaicImage, String tabName) {
		this.parentController = imageCanvasController;

		mosaicImageZoomState = MosaicImageZoomState.NO_ZOOM;
		mosaicImageView = new ImageView(mosaicImage);
		mosaicImageView.setPreserveRatio(true);
		mosaicImageAnchorPane = new AnchorPane(mosaicImageView);
		mosaicImageAnchorPane.setMinHeight(100.0);
		mosaicImageAnchorPane.setMinWidth(100.0);
		
		mosaicImageTab = new Tab(tabName, mosaicImageAnchorPane);
		mosaicImageTab.setClosable(true);
		mosaicImageTab.setOnClosed(event -> parentController.tabClosed(MosaicImageController.this));

		ChangeListener<Number> mosaicImageSizeChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateMosaicImage();
				mosaicImageZoomState = MosaicImageZoomState.NO_ZOOM;
			}
		};
		mosaicImageAnchorPane.heightProperty().addListener(mosaicImageSizeChangeListener);
		mosaicImageAnchorPane.widthProperty().addListener(mosaicImageSizeChangeListener);
		
		// Annoying order of events goes:
		//   pressed->dragged->released->clicked
		// However, if the mouse is released outside the window after dragging, the order is
		//   pressed->dragged->released
		// i.e. the clicked event is not sent
		mosaicImageAnchorPane.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				// Reset variables updated during image drag.
				mosaicImageDragOrigin = null;
				wasDragged = false;
			}
		});
		mosaicImageAnchorPane.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mosaicImageDragOrigin == null) {
					System.out.println("DRAG: Drag origin is null, creating it. originalTranslateX=" + originalTranslateX + ", originalTranslateY=" + originalTranslateY);
					mosaicImageDragOrigin = new Point2D(mouseEvent.getX(), mouseEvent.getY());
					wasDragged = true;
					originalTranslateX = mosaicImageView.getTranslateX();
					originalTranslateY = mosaicImageView.getTranslateY();
				} else {
					mosaicImageView.setTranslateX(originalTranslateX + mouseEvent.getX() - mosaicImageDragOrigin.getX());
					mosaicImageView.setTranslateY(originalTranslateY + mouseEvent.getY() - mosaicImageDragOrigin.getY());
				}
			}
			
		});
		mosaicImageAnchorPane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (wasDragged) {
					// Image was dragged, not just clicked on.
					wasDragged = false;
					mosaicImageDragOrigin = null;
					return;
				}

				ScaleTransition scaleTransition;
				switch (mosaicImageZoomState) {
				case NO_ZOOM:
					scaleTransition = new ScaleTransition(Duration.millis(250), mosaicImageView);
					scaleTransition.setToX(1.0);
					scaleTransition.setToY(1.0);
					scaleTransition.play();
					mosaicImageZoomState = MosaicImageZoomState.ONE_TO_ONE;
					break;
				case ONE_TO_ONE:
					double desiredWidth = parentController.getOriginalCanvas().getWidth();
					double imgWidth = mosaicImageView.getImage().getWidth();
					double scaleX = desiredWidth / imgWidth;
					double desiredHeight = parentController.getOriginalCanvas().getHeight();
					double imgHeight = mosaicImageView.getImage().getHeight();
					double scaleY = desiredHeight / imgHeight;

					scaleTransition = new ScaleTransition(Duration.millis(250), mosaicImageView);
					scaleTransition.setToX(scaleX);
					scaleTransition.setToY(scaleY);
					scaleTransition.play();
					mosaicImageZoomState = MosaicImageZoomState.OVERLAY_ORIGINAL;
					break;

				case OVERLAY_ORIGINAL:
					updateMosaicImage();
					mosaicImageZoomState = MosaicImageZoomState.NO_ZOOM;
					break;
				default:
					break;

				}
			}
		});
	}
	
	void resetImage(Image mosaicImage) {
		mosaicImageView.setImage(mosaicImage);
		updateMosaicImage();
	}
	
	private void updateMosaicImage() {
		double apWidth = mosaicImageAnchorPane.getWidth() - 20.0;
		double imgWidth = mosaicImageView.getImage().getWidth();
		double scaleX = apWidth / imgWidth;
		double apHeight = mosaicImageAnchorPane.getHeight() - 20.0;
		double imgHeight = mosaicImageView.getImage().getHeight();
		double scaleY = apHeight / imgHeight;
		double mosaicScaleFactor = MathUtils.min(1.0, scaleX, scaleY);
		mosaicScaleFactor = Math.max(0.25, mosaicScaleFactor);

		double translateX = 10.0 + Math.floor((apWidth - imgWidth) * 0.5);
		double translateY = 10.0 + Math.floor((apHeight - imgHeight) * 0.5);
		//System.out.println("AP width: " + apWidth + ", height: " + apHeight + ", translateX: " + translateX + ", translateY: " + translateY + ", mosaicScaleFactor: " + mosaicScaleFactor);
		mosaicImageView.setScaleX(mosaicScaleFactor);
		mosaicImageView.setScaleY(mosaicScaleFactor);
		mosaicImageView.setTranslateX(translateX);
		mosaicImageView.setTranslateY(translateY);
	}

	Tab getTab() {
		return mosaicImageTab;
	}

	

}
