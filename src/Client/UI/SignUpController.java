package Client.UI;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField LoginField;
    @FXML
    private TextField PasswordField;
    @FXML
    private TextField ConfirmPasswordField;
    @FXML
    private Button SignUpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setVisible(false);
        SignUpButton.setOnAction(event -> {
            try {
                StringBuilder com = new StringBuilder("REG ").append(LoginField.getText()).append(" ").
                        append(PasswordField.getText()).append(" ").append(ConfirmPasswordField.getText());
                if (Launch.manager.sendCommand(com.toString())) {
                    String ans = Launch.parser.parseResp();
                    if (ans != null) {
                        errorLabel.setText(ans);
                        errorLabel.setVisible(true);
                        Shake.shake(LoginField);
                        Shake.shake(PasswordField);
                        Shake.shake(ConfirmPasswordField);
                    }else{
                        errorLabel.setVisible(false);
                        SignUpButton.getScene().getWindow().hide();
                        Parent root = FXMLLoader.load(getClass().getResource("fxml/FileManager.fxml"));
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.showAndWait();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
