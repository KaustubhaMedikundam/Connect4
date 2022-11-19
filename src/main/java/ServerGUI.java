import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class JavaFXTemplate extends Application {
	static Server serverConnection;
	static ArrayList<String> actions = new ArrayList<>();
	static ListView<String> listItems;
	ListView<String> listItems2;


	public JavaFXTemplate() throws Exception {
	}

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		serverConnection = new Server(data -> {
			Platform.runLater(()->{
//				actions.add(data.toString());
				listItems.getItems().add(data.toString());
			});

		});
		launch(args);


	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Welcome to Project #2");

	     BorderPane root = new BorderPane();
	     
	     Scene scene = new Scene(root, 700,700);
			primaryStage.setScene(scene);
			primaryStage.show();
	}



}
