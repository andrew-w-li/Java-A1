import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main class for default scene and loading of the program
 */
public class Main extends Application {
	Stage primeStage;
	Scene sceneBuild, sceneDefault, sceneLibrary;
	Button goBuild, goDefault, goLibrary;
	HBox hBoxDefault = new HBox();
	Image buildImg = new Image(getClass().getResourceAsStream("new.png"));
	Image libImg = new Image(getClass().getResourceAsStream("library.png"));
	
	/**
	 * launch the program
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * UI for the default page user first sees once the program is loaded.
	 * Also, button action logic to switch scenes
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//this button will direct the user to the build command template page
		goBuild = new Button("Build a Command Page", new ImageView("new.png"));
		goBuild.setOnAction(e -> {
			VBox temp = new Builder().makeBuilder();
			temp.getChildren().add(goDefault);
			sceneBuild = new Scene(temp, 800, 600);
			primeStage.setScene(sceneBuild);
		});
		
		//this button will direct the user to the view library page
		goLibrary = new Button("View Library", new ImageView(libImg));
		goLibrary.setOnAction(e -> {
			VBox temp = new Library().makeLibrary();
			temp.getChildren().add(goDefault);
			sceneLibrary = new Scene(temp, 800, 600);
			primeStage.setScene(sceneLibrary);
		});
		
		//this button will returnthe user to the default page
		goDefault = new Button("Go Back");
		goDefault.setOnAction(e -> {
			primeStage.setScene(sceneDefault);
		});
		
		hBoxDefault.getChildren().addAll(goBuild, goLibrary);
		sceneDefault = new Scene(hBoxDefault, 800, 600);
		primeStage = primaryStage;
		primeStage.setTitle("CommandBuilder");
		primeStage.setScene(sceneDefault);
		primeStage.show();
	}//start
}
