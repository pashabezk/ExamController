package animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Fall {
    private TranslateTransition tt;

    public Fall(Node node)
    {
        tt = new TranslateTransition(Duration.millis(200), node);
        tt.setFromY(-10f);
        tt.setToY(0f);
        tt.setAutoReverse(true);
    }

    public void playAnim()
    {
        tt.playFromStart();
    }
}

