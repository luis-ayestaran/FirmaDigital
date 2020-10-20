package buap.firmaDigital.vistas;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FirmaDigital extends Application {

	private static Stage stage;
	
	public static Stage getStage() {
		return stage;
	}
	
	public static void setStage(Stage newStage) {
		stage = newStage;
	}
	
	public void lanzarVista(String args[]) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/vistas/firmaDigital.fxml"));
		try {
			StackPane root = (StackPane) loader.load();
			//FirmaDigitalControlador controlador = (FirmaDigitalControlador) loader.getController();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Firma Digital");
			primaryStage.setMaximized(true);
			primaryStage.show();
			FirmaDigital.setStage(primaryStage);
			Platform.setImplicitExit(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override  
	public void stop() throws Exception {
		Platform.exit();
		System.exit(0);
	}
	
}
