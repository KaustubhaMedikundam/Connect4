import javafx.application.Application;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
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

		launch(args);
	}

	public void startServer(Stage primaryStage, int port){

		listItems = new ListView<String>();
		serverConnection = new Server(data -> {
			Platform.runLater(()->{
				listItems.getItems().add(data.toString());
				BorderPane root = new BorderPane();
				root.setCenter(listItems);
				Scene scene = new Scene(root, 700,700);
				primaryStage.setScene(scene);
				primaryStage.show();
			});

		}, port);

	}
	public void checkPort(Stage primaryStage, int port) throws Exception {
		if(port<4000||port>8000){
			Alert fail = new Alert(Alert.AlertType.INFORMATION);
			fail.setHeaderText("failure");
			fail.setContentText("Port is not valid...try again");
			fail.showAndWait();
			start(primaryStage);
		}
		startServer(primaryStage,port);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Server");

		TextField port = new TextField();
		port.setPromptText("Enter a port number between 4000 and 8000");

		Button start = new Button();
		start.setText("Start");
//		start.setOnAction(new NewGameEvent(primaryStage));
		start.setOnAction(e-> {
			try {
				checkPort(primaryStage,  Integer.parseInt(port.getText()));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		});



		VBox vbox = new VBox(port, start);
		vbox.setAlignment(Pos.CENTER);

		BorderPane root = new BorderPane();
		root.setCenter(vbox);
		Scene scene = new Scene(root, 700,700);
		primaryStage.setScene(scene);
		primaryStage.show();
		// TODO Auto-generated method stub

	}



}