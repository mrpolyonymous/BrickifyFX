package brickifyfx;

import java.net.URL;
import java.util.ResourceBundle;

import brickifyfx.core.BrickSet;
import brickifyfx.core.ColorPalette;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class BricksAndColorsPaneController extends BasePaneController {

	@FXML private ChoiceBox<ColorPalette> colorPaletteChoice;

	@FXML private ChoiceBox<BrickSet> brickSelectionChoice;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		colorPaletteChoice.getItems().clear();
		colorPaletteChoice.getItems().addAll(ColorPalette.LDD_COLORS, ColorPalette.LDRAW_COLORS, ColorPalette.GRAYSCALE);
		colorPaletteChoice.getSelectionModel().selectFirst();

		brickSelectionChoice.getItems().clear();
		brickSelectionChoice.getItems().addAll(BrickSet.TOP_DOWN_1_BY_1_PLATE, BrickSet.TOP_DOWN_SMALL, BrickSet.TOP_DOWN, BrickSet.SIDE_ON_1_BY_1_PLATE, BrickSet.SIDE_ON);
		brickSelectionChoice.getSelectionModel().selectFirst();
	}

	public BrickSet getBrickSet() {
		return brickSelectionChoice.getValue();
	}

	public ColorPalette getColorPalette() {
		return colorPaletteChoice.getValue();
	}


}
