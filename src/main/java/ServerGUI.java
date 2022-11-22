import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ServerGUI extends Application {
	static Server serverConnection;
	static ArrayList<String> actions = new ArrayList<>();
	static ListView<String> listItems;
	ListView<String> listItems2;


	public ServerGUI() throws Exception {
	}

	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		serverConnection = new Server(data -> {
			Platform.runLater(()->{
				listItems.getItems().add(data.toString());
			});

		});
		launch(args);


	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("SERVER");
		listItems = new ListView<String>();
	     BorderPane root = new BorderPane();
		root.setCenter(listItems);
	     Scene scene = new Scene(root, 700,700);
			primaryStage.setScene(scene);
			primaryStage.show();
	}



}
