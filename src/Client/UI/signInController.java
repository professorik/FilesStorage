package Client.UI;

import Client.ClientParser;
import Warnings.CallbackGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    @FXML
    private Button SignInButton;
    @FXML
    private Button ForgetPasswordButton;
    @FXML
    private TextField LoginField;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField PasswordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
        SignInButton.setOnAction(event -> {
            try {
                String com = "LOG " + LoginField.getText() + " " + PasswordField.getText();
                if (Launch.manager.sendCommand(com)) {
                    String ans = Launch.parser.parseResp();
                    if (ans != null) {
                        errorLabel.setText(ans);
                        errorLabel.setVisible(true);
                        Shake.shake(LoginField);
                        Shake.shake(PasswordField);
                    }else{
                        errorLabel.setVisible(false);
                        Parent root = FXMLLoader.load(getClass().getResource("fxml/FileManager.fxml"));
                        SignInButton.getScene().getWindow().hide();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ForgetPasswordButton.setOnAction(event -> {
            System.out.println("OK");
        });
    }
}
