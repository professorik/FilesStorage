package Client.UI;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * @author professorik
 * @created 13/04/2021 - 17:10
 * @project Server
 */
public class Shake {
    protected static void shake(Node node){
        TranslateTransition trans = new TranslateTransition(Duration.millis(70), node);
        trans.setFromX(-10f);
        trans.setByX(10f);
        trans.setCycleCount(3);
        trans.setAutoReverse(true);
        trans.playFromStart();
    }
}
