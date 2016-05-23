package brickifyfx;

import java.net.URL;
import java.util.ResourceBundle;

import brickifyfx.quantisation.QuantisationMethod;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class RenderPaneController extends BasePaneController {
	@FXML
	private TextField outputWidthTextField;
	@FXML
	private TextField outputHeightTextField;
	@FXML
	private CheckBox maintainAspectCheckBox;
	@FXML
	private Button renderButton;
	@FXML
	private CheckBox threeDEffectCheckBox;
	@FXML
	private ChoiceBox<QuantisationMethod> quantisationChoice;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		super.initialize(url, rb);
		
		outputWidthTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			int newWidth;
			try {
				newWidth = Integer.parseInt(newValue);
			} catch (Exception e) {
				return; // TODO - handle non-numeric inputs in a more friendly way
			}
			ChoiceBox<CropRatio> cropRatioChoice = mainController.getCropPaneController().getCropRatioChoice();
			if (maintainAspectCheckBox.isSelected() && cropRatioChoice.getValue().hasRatio()) {
				int newHeight = (int) Math.round(cropRatioChoice.getValue().convertWidthToHeight(newWidth));
				outputHeightTextField.setText(String.valueOf(newHeight));
			}
		});
		
		outputHeightTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			int newHeight;
			try {
				newHeight = Integer.parseInt(newValue);
			} catch (Exception e) {
				return; // TODO - handle non-numeric inputs in a more friendly way
			}
			ChoiceBox<CropRatio> cropRatioChoice = mainController.getCropPaneController().getCropRatioChoice();
			if (maintainAspectCheckBox.isSelected() && cropRatioChoice.getValue().hasRatio()) {
				int newWidth = (int) Math.round(cropRatioChoice.getValue().convertHeightToWidth(newHeight));
				outputWidthTextField.setText(String.valueOf(newWidth));
			}
		});
		
		// TODO - restore other coloring methods that need extra options
		quantisationChoice.getItems().clear();
		quantisationChoice.getItems().addAll(QuantisationMethod.VECTOR_ERROR_DIFFUSION,
				QuantisationMethod.NAIVE_QUANTISATION_LAB,
				QuantisationMethod.NAIVE_QUANTISATION_RGB,
				QuantisationMethod.SOLID_REGIONS,
				QuantisationMethod.RESIZE_ONLY,
				QuantisationMethod.PATTERN_DITHERING);
		quantisationChoice.getSelectionModel().selectFirst();

	}

	@FXML
	protected void handleRenderButtonAction(@SuppressWarnings("unused") ActionEvent event) {
		int outputWidth = -1;
		int outputHeight = -1;
		try {
			outputWidth = Integer.parseInt(outputWidthTextField.getText());
		} catch (NumberFormatException e) {
			
		}
		try {
			outputHeight = Integer.parseInt(outputHeightTextField.getText());
		} catch (NumberFormatException e) {
			
		}
		
		mainController.render(outputWidth, outputHeight, quantisationChoice.getValue(), threeDEffectCheckBox.isSelected());
	}
}
