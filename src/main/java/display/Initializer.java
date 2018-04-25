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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.event.*;
import java.net.InetAddress;


public class Initializer extends Application{
    private static boolean isServer;
    private static String host, username;
    private static boolean done;
    private static Stage stage;

    public Initializer() {

    }

    public void startModal(){
        launch();
    }

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
        Text ipPrompt = new Text("You IP Address is - " + (localhost.getHostAddress()).trim() + "\nCopy down the IP Address for the client and then click OK");
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
            done = true;
            username = usernameField.getText();
            host = hostField.getText();
            //stage.hide();
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

        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getChildren().addAll(initialPromptBox, usernameField, hostField, submitButton, ipPrompt, closeButton);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public static boolean getIsServer(){
        return isServer;
    }

    public static String getHost(){
        return host;
    }

    public static boolean getIsDone(){
        return done;
    }

    public static String getUsername(){
        return username;
    }

    public void close(){
        //stage.hide();
        Platform.exit();
    }
}