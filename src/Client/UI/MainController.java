package Client.UI;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox vbox;
    private Parent fxml;

    public void initialize(URL url, ResourceBundle rb) {
        open_signIn();
    }

    @FXML
    private void open_signUp() {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1.0D), this.vbox);
        t.setToX(14.0D);
        t.play();
        t.setOnFinished((e) -> {
            try {
                this.fxml = FXMLLoader.load(getClass().getResource("fxml/SignUp.fxml"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            this.vbox.getChildren().removeAll(new Node[0]);
            this.vbox.getChildren().setAll(new Node[]{this.fxml});
        });
    }

    @FXML
    private void open_signIn() {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1.0D), this.vbox);
        t.setToX(this.vbox.getLayoutX() * 32.0D);
        t.play();
        t.setOnFinished((e) -> {
            try {
                this.fxml = FXMLLoader.load(getClass().getResource("fxml/SignIn.fxml"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            ;
            this.vbox.getChildren().removeAll(new Node[0]);
            this.vbox.getChildren().setAll(new Node[]{this.fxml});
        });
    }
}
