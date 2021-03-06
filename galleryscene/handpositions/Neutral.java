package galleryscene.handpositions;
import engine.animation.*;
import galleryscene.*;

public class Neutral implements HandPosition<HandConfiguration> {
    /**
    * @author Jack Deadman
    */
    public HandConfiguration getAnimationState(float t) {
        return new HandConfiguration();
    }

    public static final float[][] FINGER_VALUES = {
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, // finger 1
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, // finger 2
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f }, // finger 3
        { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f } // finger 4
    };



    public Timeline<HandConfiguration> getTimeline() {
        Timeline<HandConfiguration> timeline = new Timeline<HandConfiguration>();
        timeline.setStart(new HandConfiguration(FINGER_VALUES));
        timeline.addKeyFrame(new HandConfiguration(FINGER_VALUES), 2000);

        return timeline;
    }
}
