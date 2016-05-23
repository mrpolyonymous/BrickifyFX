package brickifyfx;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooserBuilder;

import javax.imageio.ImageIO;

import brickifyfx.core.BricksAndColors;
import brickifyfx.quantisation.QuantisationMethod;

public class OutputPaneController extends BasePaneController {
	@FXML
	private TitledPane titledPane;
	@FXML
	private Button saveImageButton;
	@FXML
	private Button saveLdrawButton;

	private File lastSaveFolder;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		titledPane.setExpanded(false);
	}

	@FXML
	protected void handleSaveImage(@SuppressWarnings("unused") ActionEvent event) {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG File", "*.png"),
				new FileChooser.ExtensionFilter("JPEG File", "*.jpg", "*.jpeg"),
				new FileChooser.ExtensionFilter("GIF File", "*.gif"));
		fileChooser.setTitle("Save Mosaic Image");

		String format = null;
		File chosenFile = showSaveDialog(fileChooser, "mosaic.png", ".png", "_mosaic");

		if (chosenFile == null) {
			return;
		}
		
		String chosenFileName = chosenFile.getName().toLowerCase();
		if (chosenFileName.endsWith(".png")) {
			format = "png";
		} else if (chosenFileName.endsWith(".jpg") || chosenFileName.endsWith(".jpeg")) {
			format = "jpg";
		} else if (chosenFileName.endsWith(".gif")) {
			format = "gif";
		} else {
			format = "png";
			chosenFile = new File(chosenFile.getParent(), chosenFile.getName() + ".png");
		}
		
		System.out.println("Save file " + chosenFile);

		try {
			ImageIO.write(getImageToWrite(), format, chosenFile);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}

		lastSaveFolder = chosenFile.getParentFile();
	}

	/**
	 * Get the image to write. This is the mosaic image with alpha channel stripped, as this
	 * doesn't work with the JPEG export.
	 */
	private BufferedImage getImageToWrite() throws InterruptedException {
		BufferedImage image = SwingFXUtils.fromFXImage(mainController.getMosaicImage(), null);
		final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};
		final ColorModel rgbOpaque = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);

		PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, true);
		pg.grabPixels();
		int width = pg.getWidth(), height = pg.getHeight();

		DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
		WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
		BufferedImage bi = new BufferedImage(rgbOpaque, raster, false, null);
		
		return bi;
	}

	@FXML
	protected void handleSaveLdraw(@SuppressWarnings("unused") ActionEvent event) throws IOException {
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("LDraw File", "*.ldr"));
		fileChooser.setTitle("Save As LDraw");

		File chosenFile = showSaveDialog(fileChooser, "mosaic.ldr", ".ldr", null);

		if (chosenFile == null) {
			return;
		}
		
		System.out.println("Save file as LDraw" + chosenFile);
		lastSaveFolder = chosenFile.getParentFile();

		new LdrawOutput().saveLdraw(chosenFile, mainController.getBricksAndColors(), mainController.getMosaic());
	}
	
	private File showSaveDialog(FileChooser fileChooser, String defaultFileName, String fileExtension, String suggestedFileSuffix) {
		File imageFile = mainController.getCurrentImageFile();
		
		File initialDirectory;
		String suggestedFileName = null;
		if (imageFile == null) {
			if (lastSaveFolder == null) {
				initialDirectory = new File(System.getProperty("user.home"));
			} else {
				initialDirectory = lastSaveFolder;
			}
			suggestedFileName = defaultFileName;
		} else {
			initialDirectory = imageFile.getParentFile();
			String fileName = imageFile.getName();
			int extensionPos = fileName.lastIndexOf('.');
			if (suggestedFileSuffix != null) {
				suggestedFileName = fileName.substring(0, extensionPos) + suggestedFileSuffix + fileName.substring(extensionPos);
			} else if (fileExtension != null) {
				suggestedFileName = fileName.substring(0, extensionPos) + fileExtension;
			}
		}

		fileChooser.setInitialDirectory(initialDirectory);
		fileChooser.setInitialFileName(suggestedFileName);

		return fileChooser.showSaveDialog(titledPane.getScene().getWindow());
		
	}

	@Override
	public void imageLoaded(Image newImage) {
		// Disable "save" fields
		saveImageButton.setDisable(true);
		saveLdrawButton.setDisable(true);
		titledPane.setExpanded(false);
	}

	@Override
	public void mosaicRendered(BricksAndColors bricksAndColors, QuantisationMethod quantisationMethod, boolean threeDEffect, Image mosaicImage) {
		saveImageButton.setDisable(false);
		saveLdrawButton.setDisable(false);
		titledPane.setExpanded(true);
	}

}
