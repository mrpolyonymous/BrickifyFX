package brickifyfx;

import java.net.URL;
import java.util.ResourceBundle;

import brickifyfx.core.BricksAndColors;
import brickifyfx.quantisation.QuantisationMethod;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;

public abstract class BasePaneController implements Initializable {
	protected BrickifyFXController mainController;

	public void setMainController(BrickifyFXController mainController) {
		this.mainController = mainController;
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	/**
	 * Allow subclasses to be notified when an image is loaded.
	 * 
	 * @param newImage
	 *            the newly loaded image
	 */
	public void imageLoaded(Image newImage) {
		// Default implementation does nothing
	}

	/**
	 * Allow subclasses to be notified when the mosaic image is generated.
	 * 
	 * @param bricksAndColors
	 *            The brick and color choices used to render the image
	 * @param quantisationMethod TODO
	 * @param threeDEffect
	 *            Was the image rendered with the 3D effect
	 * @param mosaicImage
	 *            the mosaic image
	 */
	public void mosaicRendered(BricksAndColors bricksAndColors, QuantisationMethod quantisationMethod, boolean threeDEffect, Image mosaicImage) {
		// Default implementation does nothing
	}

}
