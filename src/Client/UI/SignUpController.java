package Client.UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {

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
        SignUpButton.setOnAction(event -> {
            System.out.println(LoginField.getText());
            System.out.println(PasswordField.getText());
            System.out.println(ConfirmPasswordField.getText());
        });
    }
}
