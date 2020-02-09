package animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake //эффект встряхивания
{
    private TranslateTransition tt;
    
    public Shake(Node node)
    {
        tt = new TranslateTransition(Duration.millis(100), node);
        tt.setFromX(0f);
        tt.setByX(-7f);
        tt.setCycleCount(3);
        tt.setAutoReverse(true);
    }
    
    public void playAnim()
    {
        tt.playFromStart();
    }
}
