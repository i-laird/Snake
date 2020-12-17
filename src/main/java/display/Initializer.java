package display;

import communication.SynchronizedMessageHandler;
import communication.message.Register_Name;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Andrew Walker
 * This class creates a popup to prompt for input from the user to
 * initialize the {@link old.game.Game}
 */
public class Initializer extends Application {

    public static final String SERVER_HOST = "localhost";
    public static final int SERVER_PORT = 8078;

    private static String username;
    private static String createLobbyName;
    private Socket socket;

    // used to read and write messages
    private SynchronizedMessageHandler messageHandler = null;
    private Stage stage;

    /**
     * @author Andrew Walker
     * This function launches the modal
     */
    public static void startModal() {
        launch();
    }

    /**
     * @return the username of the player
     * @author Andrew Walker
     * This function returns the username of the player
     */
    public static String getUsername() {
        return username;
    }

    /**
     * @author Andrew Walker
     * This function closes the popup
     */
    public static void close() {
        Platform.exit();
    }

    /**
     * @param initStage the initial stage
     * @throws Exception if there is an issue generating the stage
     * @author Andrew Walker
     * This is the main function for building and displaying the popup
     */
    @Override
    public void start(Stage initStage) throws Exception {

        initSocket();

        stage = initStage;
        stage.setTitle("Snake Game");
        stage.setWidth(400);
        stage.setHeight(400);

        Text userNamePrompt = new Text("Please enter your username");
        Text lobbyNamePrompt = new Text("Please enter the lobby name");


        TextField usernameField = new TextField();
        usernameField.setPromptText("UserName");
        usernameField.setMaxWidth(125);
        usernameField.setVisible(false);

        TextField lobbyCreateName = new TextField();
        lobbyCreateName.setPromptText("Enter lobby name");
        lobbyCreateName.setMaxWidth(125);
        lobbyCreateName.setVisible(false);

        Button showLobbies = new Button("Show Lobby");
        Button createLobby = new Button("Create Lobby");
        createLobby.setOnMouseClicked(e ->{
            lobbyCreateName.setVisible(true);
        });

        Button closeButton = new Button("Ready");

        Button submit = new Button("Submit");
        submit.setMinWidth(80);
        submit.setVisible(true);
        submit.setOnMouseClicked(e -> {
            if(usernameField.isVisible()){
                username = usernameField.getText();
                registerUserName();
                usernameField.setDisable(true);
                usernameField.setVisible(false);
                showLobbies.setVisible(true);
                createLobby.setVisible(true);
                submit.setVisible(false);
            }
            else if(lobbyCreateName.isVisible()){
                createLobbyName = lobbyCreateName.getText();
                lobbyCreateName.setDisable(true);
                lobbyCreateName.setVisible(false);
                submit.setVisible(false);
            }
        });

        closeButton.setMinWidth(80);
        closeButton.setOnMouseClicked(e -> Platform.exit());
        closeButton.setVisible(false);

        HBox userNamePromptHBox = new HBox();
        userNamePromptHBox.getChildren().addAll(userNamePrompt);
        userNamePromptHBox.setSpacing(5);
        userNamePromptHBox.setPadding(new Insets(10, 10, 10, 10));
        userNamePromptHBox.setAlignment(Pos.CENTER);

        VBox userNameBox = new VBox();
        userNameBox.getChildren().addAll(userNamePromptHBox, usernameField);
        userNameBox.setSpacing(5);
        userNameBox.setPadding(new Insets(10, 10, 10, 10));
        userNameBox.setAlignment(Pos.CENTER);

        VBox lobbyNameBox = new VBox();
        lobbyNameBox.getChildren().addAll(lobbyNamePrompt, lobbyCreateName);
        lobbyNameBox.setSpacing(5);
        lobbyNameBox.setPadding(new Insets(10, 10, 10, 10));
        lobbyNameBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(userNameBox, lobbyNameBox, submit, closeButton);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public void initSocket(){
        try {
            this.socket = new Socket(SERVER_HOST, SERVER_PORT);
            this.messageHandler = new SynchronizedMessageHandler(this.socket);
        }catch(IOException e){
            close();
        }
    }

    public void registerUserName() throws IOException {
        this.messageHandler.sendMessage(new Register_Name(username));
    }
}