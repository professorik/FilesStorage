package Client.UI;

import Client.ClientParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Launch extends Application {

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Main.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        /*root.setOnDragDetected(mouseDragEvent -> {
            stage.setX(mouseDragEvent.getX());
            stage.setY(mouseDragEvent.getY());
        });*/
        connect();
        stage.setOnCloseRequest(windowEvent -> manager.exit());
        stage.show();
    }

    private final String host = "localhost";//"192.168.0.228";
    private int port = 3333;
    protected static ClientParserUI parser;
    protected static ClientFileManagerUI manager;

    private void connect() {
        Socket socket;
        DataOutputStream outputStream;
        DataInputStream inputStream;
        try {
            socket = new Socket(host, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            parser = new ClientParserUI(inputStream);
            manager = new ClientFileManagerUI(inputStream, outputStream, socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
