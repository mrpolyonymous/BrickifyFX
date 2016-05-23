package brickifyfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import brickifyfx.core.BricksAndColors;
import brickifyfx.core.Calculation;
import brickifyfx.core.InterpolationMethod;
import brickifyfx.core.Mosaic;
import brickifyfx.core.MosaicImageGenerator;
import brickifyfx.quantisation.Quantisation;
import brickifyfx.quantisation.QuantisationMethod;
import brickifyfx.tiling.Tiling;
import brickifyfx.tiling.TilingMethod;

/**
 * FXML Controller class
 * 
 * @author Dan
 */
public class BrickifyFXController implements Initializable {

	@FXML
	private SplitPane mainWindowSplitPane;
	@FXML
	private AnchorPane originalImagePane;
	@FXML
	private VBox titledPaneBox;

	// List of all loaded controllers for easy looping/message sending to all controllers
	private List<BasePaneController> loadedControllers;
	
	private ImageCanvasController imageCanvasController;

	// File chooser fields
	private LoadPaneController loadPaneController;

	// Crop-related fields
	private CropPaneController cropPaneController;
	
	// Bricks and color-related fields
	private BricksAndColorsPaneController bricksAndColorsPaneController;

	// Rendering-related fields
	private RenderPaneController renderPaneController;

	// Save-related fields
	private OutputPaneController outputPaneController;

	private Image originalImage;
	private Image mosaicImage;
	private Mosaic mosaic;
	private BricksAndColors bricksAndColors;
	private File currentImageFile;

	public BrickifyFXController() {
		loadedControllers = new ArrayList<>();
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		originalImage = new Image(getClass().getResourceAsStream("/warhol.jpg"));

		
    	FXMLLoader fxmlLoader;
        try {
        	fxmlLoader = new FXMLLoader(getClass().getResource("ImageCanvas.fxml"));
			TabPane canvasPane = (TabPane) fxmlLoader.load();
			imageCanvasController = fxmlLoader.getController();
			imageCanvasController.setMainController(this);
			originalImagePane.getChildren().add(canvasPane);
			AnchorPane.setTopAnchor(canvasPane, Double.valueOf(0.0));
			AnchorPane.setRightAnchor(canvasPane, Double.valueOf(0.0));
			AnchorPane.setBottomAnchor(canvasPane, Double.valueOf(0.0));
			AnchorPane.setLeftAnchor(canvasPane, Double.valueOf(0.0));
			loadedControllers.add(imageCanvasController);

			fxmlLoader = new FXMLLoader(getClass().getResource("LoadPane.fxml"));
			TitledPane loadPane = (TitledPane) fxmlLoader.load();
			loadPaneController = fxmlLoader.getController();
			loadPaneController.setMainController(this);
			loadPaneController.setInfoText((int)originalImage.getWidth() + " by " + (int)originalImage.getHeight() + " pixels");
			titledPaneBox.getChildren().add(loadPane);
			loadedControllers.add(loadPaneController);

			fxmlLoader = new FXMLLoader(getClass().getResource("CropPane.fxml"));
			TitledPane cropPane = (TitledPane) fxmlLoader.load();
			cropPane.setExpanded(false);
			cropPaneController = fxmlLoader.getController();
			cropPaneController.setMainController(this);
			cropPaneController.imageLoaded(originalImage);
			titledPaneBox.getChildren().add(cropPane);
			loadedControllers.add(cropPaneController);
			
			// TODO - all the circular referencing is icky
			imageCanvasController.setCropPaneController(cropPaneController);

			fxmlLoader = new FXMLLoader(getClass().getResource("BricksAndColorsPane.fxml"));
			TitledPane bricksAndColorsPane = (TitledPane) fxmlLoader.load();
			bricksAndColorsPane.setExpanded(false);
			bricksAndColorsPaneController = fxmlLoader.getController();
			bricksAndColorsPaneController.setMainController(this);
			titledPaneBox.getChildren().add(bricksAndColorsPane);
			loadedControllers.add(bricksAndColorsPaneController);


			fxmlLoader = new FXMLLoader(getClass().getResource("RenderPane.fxml"));
			TitledPane renderPane = (TitledPane) fxmlLoader.load();
			renderPaneController = fxmlLoader.getController();
			renderPaneController.setMainController(this);
			titledPaneBox.getChildren().add(renderPane);
			loadedControllers.add(renderPaneController);


			fxmlLoader = new FXMLLoader(getClass().getResource("OutputPane.fxml"));
			TitledPane outputPane = (TitledPane) fxmlLoader.load();
			outputPaneController = fxmlLoader.getController();
			outputPaneController.setMainController(this);
			titledPaneBox.getChildren().add(outputPane);
			loadedControllers.add(outputPaneController);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public void loadImage(File imageFile) throws FileNotFoundException, IOException {
		try (FileInputStream fis = new FileInputStream(imageFile)) {
			originalImage = new Image(fis);
		}
		
		currentImageFile = imageFile;
		
		// Reset mosaic
		mosaicImage = null;
		bricksAndColors = null;
		mosaic = null;
	
		for (BasePaneController basePaneController : loadedControllers) {
			basePaneController.imageLoaded(originalImage);
		}
		System.gc();
	}


	public ImageCanvasController getImageCanvasController() {
		return imageCanvasController;
	}


	public CropPaneController getCropPaneController() {
		return cropPaneController;
	}

	public Image getOriginalImage() {
		return originalImage;
	}

	public File getCurrentImageFile() {
		return currentImageFile;
	}

	public Image getMosaicImage() {
		return mosaicImage;
	}

	public BricksAndColors getBricksAndColors() {
		return bricksAndColors;
	}

	public Mosaic getMosaic() {
		return mosaic;
	}

	protected void render(int outputWidth, int outputHeight, QuantisationMethod quantisationMethod, boolean threeDEffect) {
		
		long start = System.nanoTime();

		double longestDesiredDimension;
		int mosaicWidth;
		int mosaicHeight;
		Image mosaicSourceImage;

		Rectangle2D originalImageCropRectangle = imageCanvasController.getOriginalImageCropRectangle();
		if (originalImageCropRectangle == null) {
			mosaicSourceImage = originalImage;
			double scale;
			if ( outputWidth > 0 && outputHeight > 0) {
				mosaicWidth = outputWidth;
				mosaicHeight = outputHeight;
			} else {
				if (outputWidth > 0) {
					scale = outputWidth / mosaicSourceImage.getWidth();
					mosaicWidth = outputWidth;
					mosaicHeight = (int) Math.round(mosaicSourceImage.getHeight() * scale);
				} else if (outputHeight > 0) {
					scale = outputHeight / mosaicSourceImage.getHeight();
					mosaicWidth = (int) Math.round(mosaicSourceImage.getWidth() * scale);
					mosaicHeight = outputHeight;
				} else {
					longestDesiredDimension = 64;
					if (mosaicSourceImage.getWidth() >= mosaicSourceImage.getHeight()) {
						// landscape
						scale = longestDesiredDimension / mosaicSourceImage.getWidth();
					} else {
						// portrait
						scale = longestDesiredDimension / mosaicSourceImage.getHeight();
					}
					scale = Math.min(1.0, scale);
					mosaicWidth = (int) Math.round(mosaicSourceImage.getWidth() * scale);
					mosaicHeight = (int) Math.round(mosaicSourceImage.getHeight() * scale);
				}
	
			}
		} else {
			mosaicSourceImage = new WritableImage(originalImage.getPixelReader(),
					(int) originalImageCropRectangle.getMinX(),
					(int) originalImageCropRectangle.getMinY(),
					(int) originalImageCropRectangle.getWidth(),
					(int) originalImageCropRectangle.getHeight());
			// COMPLAINT - why do we suddenly live in a world of int parameters?

			if ( outputWidth > 0 && outputHeight > 0) {
				mosaicWidth = outputWidth;
				mosaicHeight = outputHeight;
			} else {
				double scale;
				if (outputWidth > 0) {
					scale = outputWidth / mosaicSourceImage.getWidth();
					mosaicWidth = outputWidth;
					mosaicHeight = (int) Math.round(mosaicSourceImage.getHeight() * scale);
				} else if (outputHeight > 0) {
					scale = outputHeight / mosaicSourceImage.getHeight();
					mosaicWidth = (int) Math.round(mosaicSourceImage.getWidth() * scale);
					mosaicHeight = outputHeight;
				} else {
					longestDesiredDimension = 64;
					double cropWidth = originalImageCropRectangle.getWidth();
					double cropHeight = originalImageCropRectangle.getHeight();
					if (cropWidth >= cropHeight) {
						scale = longestDesiredDimension / cropWidth; 
					} else {
						scale = longestDesiredDimension / cropHeight;
					}
					scale = Math.min(1.0, scale);
					
					mosaicWidth = (int) Math.round(cropWidth * scale);
					mosaicHeight = (int) Math.round(cropHeight * scale);
				}

			}
		}
		
		mosaic = new Mosaic(mosaicWidth, mosaicHeight);
		bricksAndColors = new BricksAndColors(bricksAndColorsPaneController.getBrickSet(), bricksAndColorsPaneController.getColorPalette());
		
		long startMemory = Runtime.getRuntime().freeMemory();
		Calculation calculation = new Calculation();
		
		BufferedImage cutout = calculation.scale(SwingFXUtils.fromFXImage(mosaicSourceImage, null),
				mosaicWidth,
				mosaicHeight,
				InterpolationMethod.BICUBIC);
		int[] rgbPixels = cutout.getRGB(0, 0, mosaicWidth, mosaicHeight, null, 0, mosaicWidth);
		long end = System.nanoTime();
		System.out.format("Image resizing and setup done in in %,dns\n", Long.valueOf(end-start));

		start = System.nanoTime();
		Quantisation quantisation = quantisationMethod.getQuantisation();
		quantisation.quantisation(bricksAndColors, mosaic, rgbPixels);
		end = System.nanoTime();
		long endMemory = Runtime.getRuntime().freeMemory();
		System.out.format("Quantisation done in %,dns, or %,d bricks/s\n", Long.valueOf(end-start), Long.valueOf(1000000000L * mosaicWidth * mosaicHeight/(end-start)));
		System.out.format("Memory consumed: %,d bytes\n",  Long.valueOf(startMemory-endMemory));

		start = System.nanoTime();
		Tiling tiling;
		if (bricksAndColors.getBricks().isBasicBrickOnly()) {
			tiling = TilingMethod.BASIC_BRICKS_ONLY.getTiling();
		} else {
			// TODO - restore stability optimization choice
			tiling = TilingMethod.BRICK_SIZE_OPTIMISATION.getTiling();
		}
		tiling.tiling(bricksAndColors, mosaic);
		end = System.nanoTime();
		System.out.format("Tiling done in %,dns\n", Long.valueOf(end-start));

		start = System.nanoTime();
		MosaicImageGenerator imageGenerator = new MosaicImageGenerator(bricksAndColors, threeDEffect, false);
		mosaicImage = imageGenerator.generateMosaicImage(mosaic);
		end = System.nanoTime();
		System.out.format("Image generation done in %,dns\n", Long.valueOf(end-start));

		
		for (BasePaneController basePaneController : loadedControllers) {
			basePaneController.mosaicRendered(bricksAndColors, quantisationMethod, threeDEffect, mosaicImage);
		}

		// Collect garbage. Not actually necessary, but allows better measurement of how
		// much memory is allocated when rendering an image
		System.gc();
	}

}
