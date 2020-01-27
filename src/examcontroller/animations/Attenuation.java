package examcontroller.animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Attenuation //эффект затухания
{
    Timeline timeline;
    
    public Attenuation(Node node)
    {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(node.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(node.opacityProperty(), 0.0)));
    }
    
    public void playAnim()
    {
        timeline.play();
    }
}
