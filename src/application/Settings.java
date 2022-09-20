package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Settings extends Application {
	private static Stage stage;
	
	private static SettingsController controller;
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		Platform.runLater(new Runnable() {
			@Override public void run() {
				try {
					Platform.setImplicitExit(false);
					
					FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("settings.fxml"));
					
					Parent root = loader.load();
					
					controller = (SettingsController)loader.getController();
					
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getClassLoader().getResource("application.css").toExternalForm());
					
					primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/simplify3d-logo.png")));
					primaryStage.setScene(scene);
					primaryStage.hide();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void updateAutoRun() {
		controller.updateAutoRun();
	}
	
	public static void updateAutoStart() {
		controller.updateAutoStart();
	}
 	
	public static void showStage() {
		Platform.runLater(() -> stage.show());
	}
	
	public static void hideStage() {
		Platform.runLater(() -> stage.hide());
	}

	public static void main(String[] args) {
		launch();
	}
}
