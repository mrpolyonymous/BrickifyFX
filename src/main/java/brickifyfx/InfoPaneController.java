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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;

import brickifyfx.core.BricksAndColors;
import brickifyfx.quantisation.QuantisationMethod;

public class InfoPaneController extends BasePaneController {
	@FXML
	private TitledPane titledPane;
	@FXML
	private Button aboutButton;
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		titledPane.setExpanded(false);
	}

	@FXML
	protected void handleAbout(@SuppressWarnings("unused") ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Notice and Disclaimers");
        alert.setContentText("BrickifyFX - Turn images in to mosaics you can build with your favourite plastic brick construction toy.\n\n" +

			    "This program is free software: you can redistribute it and/or modify " +
			    "it under the terms of the GNU General Public License as published by " +
			    "the Free Software Foundation, either version 3 of the License, or " +
			    "(at your option) any later version.\n\n" +
			
			    "This program is distributed in the hope that it will be useful, " +
			    "but WITHOUT ANY WARRANTY; without even the implied warranty of " +
			    "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the " +
			    "GNU General Public License for more details.\n\n" +

			    "Disclaimer: LEGO® is a trademark of the LEGO Group of companies which does not sponsor, authorize or endorse this application\n"
        		);
        alert.show();
	}

}
