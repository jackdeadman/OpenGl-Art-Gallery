// Main Assignemnt class
import engine.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import scene.*;
import models.*;
public class Arty {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);

    private static Vec3 CAMERA_START_POSITION = new Vec3(0f, 6f, 14.0f);
    private static HandConfiguration handConfiguration;

    private static void addSlider(JPanel panel, String text, float amount, ChangeListener listener) {
        JPanel fingerControl = new JPanel();
        JSlider slider = new JSlider(JSlider.HORIZONTAL, (int)(amount * 100), 100, 0);
        JLabel label = new JLabel(text);
        JLabel valueLabel = new JLabel(String.valueOf(amount));


        fingerControl.add(label);
        fingerControl.add(slider);
        fingerControl.add(valueLabel);

        slider.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent evt) {
            JSlider slider = (JSlider) evt.getSource();
            // if (!slider.getValueIsAdjusting()) {
              int value = slider.getValue();
              valueLabel.setText(String.valueOf(value / 100));
              listener.stateChanged(evt);
            // }
          }
        });

        panel.add(fingerControl);
    }


    public static void main(String[] args) {

        handConfiguration = new HandConfiguration();

        Camera camera = new Camera(CAMERA_START_POSITION,
                                    Camera.DEFAULT_TARGET,
                                    Camera.DEFAULT_UP);

        AnimationEngine<HandConfiguration> animator = new AnimationEngine<>();
        GalleryScene gallery = new GalleryScene(camera, handConfiguration, animator);

        Lamp[] lamps = gallery.getLampModels();
        DirectionalLight worldLight = gallery.getWorldLight();

        GraphicsWindow window = new GraphicsWindow("Art Gallery", gallery);
        CameraController controller = new CameraController(camera);

        window.addMouseMotionListener(controller);
        window.addKeyListener(controller);

        window.start();

        SceneControls controls = new SceneControls(handConfiguration);
        controls.setLampModels(lamps);
        controls.setWorldLight(worldLight);
        controls.setAnimationEngine(animator);
        controls.setCamera(camera);

        JScrollPane scrollFrame = new JScrollPane(controls);
        controls.setAutoscrolls(true);
        scrollFrame.setPreferredSize(new Dimension( 330, window.getHeight()));

        window.getContentPane().add(scrollFrame, BorderLayout.EAST);
        window.setSize(dimension);
        window.setVisible(true);
    }

}
