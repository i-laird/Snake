package display;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.awt.event.*;
import java.net.InetAddress;

/**
 * This class creates a popup to prompt for imput from the user to
 * initialize the {@link game.Game}
 */
public class Initializer extends Application{
    private static boolean isServer;
    private static String host, username;
    private static Stage stage;

    /**
     * This function launches the modal
     */
    public static void startModal(){
        launch();
    }

    /**
     * This is the main function for building and displaying the popup
     * @param initStage the initial stage
     * @throws Exception
     */
    @Override
    public void start(Stage initStage) throws Exception {

        stage = initStage;
        stage.setTitle("Snake");
        stage.setWidth(400);
        stage.setHeight(400);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(125);
        usernameField.setVisible(false);

        Text initialPrompt = new Text("Will you be a server or a client?");
        InetAddress localhost = InetAddress.getLocalHost();
        Text ipPrompt = new Text("You IP Address is - " + (localhost.getHostAddress()).trim() + "\nCopy down the IP Address for the client and then click OK \n(This window will close)");
        ipPrompt.setVisible(false);

        TextField hostField = new TextField();
        hostField.setPromptText("Hostname");
        hostField.setMaxWidth(125);
        hostField.setVisible(false);


        Button serverButton = new Button("Server");
        Button clientButton = new Button("Client");
        Button closeButton = new Button("OK");

        Button submitButton = new Button("Submit");
        submitButton.setMinWidth(80);
        submitButton.setOnMouseClicked(e -> {
            username = usernameField.getText();
            host = hostField.getText();
            if(isServer) {
                ipPrompt.setVisible(true);
                submitButton.setDisable(true);
                clientButton.setDisable(true);
                serverButton.setDisable(true);
                usernameField.setDisable(true);
                closeButton.setVisible(true);
            } else {
                stage.close();
            }
        });
        submitButton.setVisible(false);


        closeButton.setMinWidth(80);
        closeButton.setOnMouseClicked(e -> {
            Platform.exit();
        });
        closeButton.setVisible(false);

        serverButton.setMinWidth(80);
        serverButton.setOnMouseClicked(e -> {
            isServer = true;
            usernameField.setVisible(true);
            hostField.setVisible(false);
            submitButton.setVisible(true);
        });

        clientButton.setMinWidth(80);
        clientButton.setOnMouseClicked(e -> {
            isServer = false;
            usernameField.setVisible(true);
            hostField.setVisible(true);
            submitButton.setVisible(true);
        });



        HBox clientServer = new HBox();
        clientServer.getChildren().addAll(serverButton, clientButton);
        clientServer.setSpacing(5);
        clientServer.setPadding(new Insets(10, 10, 10, 10));
        clientServer.setAlignment(Pos.CENTER);

        HBox initalPromptHbox = new HBox();
        initalPromptHbox.getChildren().addAll(initialPrompt);
        initalPromptHbox.setSpacing(5);
        initalPromptHbox.setPadding(new Insets(10, 10, 10, 10));
        initalPromptHbox.setAlignment(Pos.CENTER);

        VBox initialPromptBox = new VBox();
        initialPromptBox.getChildren().addAll(initalPromptHbox, clientServer);
        initialPromptBox.setSpacing(5);
        initialPromptBox.setPadding(new Insets(10, 10, 10, 10));
        clientServer.setAlignment(Pos.CENTER);

        StackPane ipRoot = new StackPane();
        ipPrompt.setTextAlignment(TextAlignment.CENTER);
        ipRoot.getChildren().addAll(ipPrompt);
        StackPane.setAlignment(ipPrompt, Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(initialPromptBox, usernameField, hostField, submitButton, ipRoot, closeButton);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Returns if it is going to be a server game
     * @return if it is going to be a server game
     */
    public static boolean getIsServer(){
        return isServer;
    }

    /**
     * Gets the hostname for the connection
     * @return the hostname for the connection
     */
    public static String getHost(){
        return host;
    }

    /**
     * This function returns the username of the player
     * @return
     */
    public static String getUsername(){
        return username;
    }

    /**
     * This function closes the popup
     */
    public static void close(){
        Platform.exit();
    }
}