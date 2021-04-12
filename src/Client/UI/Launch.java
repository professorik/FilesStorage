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

    protected static Parent signIn;
    protected static Parent signUp;

    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/Main.fxml"));
        signIn = FXMLLoader.load(getClass().getResource("fxml/SignIn.fxml"));
        signUp = FXMLLoader.load(getClass().getResource("fxml/SignUp.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        connect();
        stage.setOnCloseRequest(windowEvent -> {
            try {
                parser.parseRequest("EXIT");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stage.show();
    }

    private final static String host = "localhost";//"192.168.0.228";
    private static int port = 3333;
    protected static ClientParser parser;

    private void connect() {
        Socket socket;
        DataOutputStream outputStream;
        DataInputStream inputStream;
        try {
            socket = new Socket(host, port);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
            parser = new ClientParser(inputStream, outputStream, socket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
