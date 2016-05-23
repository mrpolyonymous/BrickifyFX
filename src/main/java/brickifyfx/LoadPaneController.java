package brickifyfx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

public class LoadPaneController extends BasePaneController implements Initializable {
	// File chooser fields
	@FXML
	private Button loadButton;
	@FXML
	private TextField imageFileTextField;
	@FXML
	private TextField imageInfoField;

	private File lastLoadedFolder;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	public void handleLoadButtonAction(@SuppressWarnings("unused") ActionEvent event) throws FileNotFoundException, IOException {
		
		File initialDirectory;
		if (lastLoadedFolder == null) {
			initialDirectory = new File(System.getProperty("user.home"));
		} else {
			initialDirectory = lastLoadedFolder;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
		fileChooser.setInitialDirectory(initialDirectory);
		fileChooser.setTitle("Choose Image");

		File chosenFile = fileChooser.showOpenDialog(loadButton.getScene().getWindow());

		if (chosenFile == null) {
			return;
		}

		mainController.loadImage(chosenFile);

		imageFileTextField.setText(chosenFile.getAbsolutePath());
		lastLoadedFolder = chosenFile.getParentFile();
		
	}

	public void setInfoText(String infoText) {
		imageInfoField.setText(infoText);
		
	}

	@Override
	public void imageLoaded(Image newImage) {
		imageInfoField.setText((int)newImage.getWidth() + " by " + (int)newImage.getHeight() + " pixels");
	}

}
