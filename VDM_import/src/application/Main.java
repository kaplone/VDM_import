package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	private static Stage stage;
	private static Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		
		this.stage = primaryStage;
		
		try {
			scene = new Scene((Parent) JfxUtils.loadFxml("VDM_import_GUI.fxml"), 1020, 800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setAlwaysOnTop(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getStage() {
		return stage;
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	
}
