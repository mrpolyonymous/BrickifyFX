package brickifyfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

public class CropPaneController extends BasePaneController {
	@FXML
	private ChoiceBox<CropRatio> cropRatioChoice;
	@FXML
	private TextField cropOriginXTextField;
	@FXML
	private TextField cropOriginYTextField;
	@FXML
	private TextField cropWidthTextField;
	@FXML
	private TextField cropHeightTextField;
	@FXML
	private Button cropButton;

	@Override
	public void imageLoaded(Image newImage) {
		cropRatioChoice.getItems().clear();
		cropRatioChoice.getItems().addAll(new NoCrop("None"),
				new WidthByHeightRatio("Original Image", newImage.getWidth(), newImage.getHeight()),
				new WidthByHeightRatio("Square", 1, 1),
				new WidthByHeightRatio("4 by 3", 4, 3),
				new WidthByHeightRatio("3 by 4", 3, 4),
				new WidthByHeightRatio("10 by 8", 10, 8),
				new WidthByHeightRatio("8 by 10", 8, 10));
		cropRatioChoice.getSelectionModel().selectFirst();
		
		resetCrop();
	}

	void updateCropTextFields() {
		ImageCanvasController imageCanvasController = mainController.getImageCanvasController();
		double cropWidth = Math.abs(imageCanvasController.getCanvasCropToX() - imageCanvasController.getCanvasCropOriginX());
		double cropHeight = Math.abs(imageCanvasController.getCanvasCropToY() - imageCanvasController.getCanvasCropOriginY());
		cropOriginXTextField.setText(String.valueOf((int)Math.round(imageCanvasController.getCanvasCropOriginX()/imageCanvasController.getScaleFactor())));
		cropOriginYTextField.setText(String.valueOf((int)Math.round(imageCanvasController.getCanvasCropOriginY()/imageCanvasController.getScaleFactor())));
		cropWidthTextField.setText(String.valueOf((int)Math.round(cropWidth/imageCanvasController.getScaleFactor())));
		cropHeightTextField.setText(String.valueOf((int)Math.round(cropHeight/imageCanvasController.getScaleFactor())));
	}

	void resetCrop() {
		cropOriginXTextField.setText("");
		cropOriginYTextField.setText("");
		cropWidthTextField.setText("");
		cropHeightTextField.setText("");
	}

	public ChoiceBox<CropRatio> getCropRatioChoice() {
		return cropRatioChoice;
	}

	@FXML
	protected void handleCropMode(@SuppressWarnings("unused") ActionEvent event) {
		mainController.getImageCanvasController().switchCropMode();
	}

	@FXML
	protected void handleClearCrop(@SuppressWarnings("unused") ActionEvent event) {
		resetCrop();
		mainController.getImageCanvasController().handleClearCrop();
	}

	void updateCropModeStyles(boolean cropMode) {
		if (cropMode) {
			cropButton.getStyleClass().add("selectedButton");
		} else {
			cropButton.getStyleClass().remove("selectedButton");
		}
	}

}
