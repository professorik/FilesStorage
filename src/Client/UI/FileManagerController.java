package Client.UI;

/**
 * @author professorik
 * @created 14/04/2021 - 12:26
 * @project Server
 */

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class FileManagerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button backBtn;

    @FXML
    private TextField pathField;

    @FXML
    private GridPane gridPane;

    private Stage stage;

    private static String img1;
    private static String img2;

    public FileManagerController() throws URISyntaxException {
        img1 = getClass().getResource("resources/files.png").toURI().toString();
        img2 = getClass().getResource("resources/folder.png").toURI().toString();
    }

    @FXML
    void initialize() throws IOException {
        //stage = (Stage) backBtn.getScene().getWindow();
        backBtn.setOnAction(actionEvent -> {
            System.out.println("HI");
        });
        setGrid();
        /*
        stage.setOnCloseRequest(windowEvent -> {
            try {
                Launch.parser.parseRequest("EXIT");
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        });*/
    }

    private void setGrid() throws IOException {
        gridPane.getChildren().clear();
        JSONArray info = Launch.manager.lookDirInfo();
        Iterator i = info.iterator();
        gridPane.setGridLinesVisible(true);
        for (int j = 0; 2 * j < info.size(); j++) {
            for (int k = 0; k < 2 && i.hasNext(); k++) {
                JSONObject file = (JSONObject) i.next();
                Grid grid = new Grid(new Date((long) file.get("time")), (boolean) file.get("isDir"), (String) file.get("name"));
                gridPane.add(grid.getRoot(), k, j);
            }
        }
    }

    class Grid {
        private HBox root;
        private Date date;
        private boolean isFolder;
        private String name;

        public Grid(Date date, boolean isFolder, String name) {
            this.date = date;
            this.isFolder = isFolder;
            this.name = name;
            ImageView icon = new ImageView(isFolder ? img2 : img1);
            Label label = new Label(name);
            label.setStyle("-fx-text-fill: white");
            root = new HBox(icon, label);
            root.setOnMouseClicked(mouseEvent -> open(mouseEvent));
        }

        private void open(MouseEvent mouseEvent){
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                if (mouseEvent.getClickCount() == 2 && isFolder){
                    try {
                        System.out.println(date + " " + name);
                        Launch.manager.sendCommand("CD " + name);
                        setGrid();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public HBox getRoot() {
            return root;
        }
    }
}
