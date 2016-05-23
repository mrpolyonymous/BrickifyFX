package brickifyfx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application entry point.
 */
public class BrickifyFX extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("BrickifyFX.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Scene scene = new Scene(root);

		stage.setTitle("BrickifyFX");
		stage.setMinWidth(700.0);
		stage.setMinHeight(400.0);
		stage.setScene(scene);
		stage.show();
	}

	// The following comment is from NetBeans circa 2013. "deployment artifacts" seemed to mean
	// many MB of extra classes. This seems to work just fine to launch the application under
	// Java 8.
	/**
	 * The main() method is ignored in correctly deployed JavaFX application. main() serves only as
	 * fallback in case the application can not be launched through deployment artifacts, e.g., in
	 * IDEs with limited FX support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
