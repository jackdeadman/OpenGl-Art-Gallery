package galleryscene;

import com.jogamp.opengl.*;

import engine.*;
import engine.animation.*;
import engine.lighting.*;
import engine.render.*;
import engine.scenegraph.*;

import gmaths.*;
import models.*;

public class GalleryScene extends Scene {
    /**
    * @author Jack Deadman
    */
    // Models in the scene
    private Hand hand;
    private Room room;
    private Lamp lamp1, lamp2;
    private Arm arm;
    private PictureFrame pictureFrame;
    private Sky skyBox;

    private HandConfiguration handConfiguration;
    private final Vec3 DIRECTIONAL_LIGHT_DIR = new Vec3(0.2f, -0.2f, 0.3f);
    private AnimationEngine<HandConfiguration> animator;

    private PictureFrame[] pictureFrames = new PictureFrame[6];
    private final String IMAGE_BASE = "textures/hands/hand";


    public GalleryScene(Camera camera, HandConfiguration handConfiguration, AnimationEngine<HandConfiguration> animator) {
        super(camera);

        this.handConfiguration = handConfiguration;
        this.animator = animator;

        Vec3 colour = new Vec3(0.1f, 0.1f, 0.1f);
        DirectionalLight light = new DirectionalLight(DIRECTIONAL_LIGHT_DIR, colour);
        setDirectionalLight(light);

        setupModels();
    }

    public Lamp[] getLampModels() {
        return new Lamp[] { lamp1, lamp2 };
    }

    public DirectionalLight getWorldLight() {
        return getDirectionalLight();
    }


    private void setupModels() {
        lamp1 = new Lamp(worldConfig);
        lamp2 = new Lamp(worldConfig);
        room = new Room(worldConfig, 16, 28, 10);
        hand = new Hand(worldConfig, handConfiguration);
        arm = new Arm(worldConfig);
        skyBox = new Sky(worldConfig);

        for (int i=0; i<6; ++i) {
            String file = IMAGE_BASE + (i+1) + ".jpg";
            PictureFrame.PictureDimension dimension = (i == 1 || i == 4)
                    ? PictureFrame.VERTICAL_FRAME : PictureFrame.VERTICAL_FRAME_SMALL;
            pictureFrames[i] = new PictureFrame(worldConfig, dimension, file);
        }

        registerModels(new Model[] { arm, lamp1, lamp2, room, hand, skyBox });
        registerModels(pictureFrames);
    }

    private void addFramesToWalls() {
        room.addPictureToWall(Room.WallPosition.LEFT_CLOSE, pictureFrames[0]);
        room.addPictureToWall(Room.WallPosition.LEFT_MIDDLE, pictureFrames[1]);
        room.addPictureToWall(Room.WallPosition.LEFT_FAR, pictureFrames[2]);
        room.addPictureToWall(Room.WallPosition.RIGHT_CLOSE, pictureFrames[3]);
        room.addPictureToWall(Room.WallPosition.RIGHT_MIDDLE, pictureFrames[4]);
        room.addPictureToWall(Room.WallPosition.RIGHT_FAR, pictureFrames[5]);
    }


    protected void buildSceneGraph(GL3 gl) {
        addFramesToWalls();

        SGNode scene = new NameNode("Gallery Scene");

        TransformNode moveLight1 = new TransformNode("Move lamp 1",
                Mat4Transform.translate(-7.0f, 0.0f, -12.0f));

        TransformNode moveLight2 = new TransformNode("Move lamp 2",
                Mat4Transform.translate(7.0f, 0.0f, 4.0f));

        TransformNode moveHandIntoArm = new TransformNode(
            "Move hand into arm",
            Mat4Transform.translate(0f, -0.75f, 0f)
        );

        scene.addChild(room.getRoot());
        room.getAnchor().addChild(moveLight1);
                moveLight1.addChild(lamp1.getRoot());
        room.getAnchor().addChild(moveLight2);
                moveLight2.addChild(lamp2.getRoot());
        room.getAnchor().addChild(arm.getRoot());
            arm.getAnchor().addChild(moveHandIntoArm);
                moveHandIntoArm.addChild(hand.getRoot());

        scene.update();
        setSceneNode(scene);
    }

    protected void beforeSceneDraw(GL3 gl) {
        // Sky is not part of the scene graph.
        // Also needs to be render first so alpha blending works
        // correctly.
         skyBox.render(gl);
    }

    protected void update(GL3 gl) {
        // Update the hand based on the animation
        handConfiguration = animator.getAnimationState();
        hand.setConfiguration(handConfiguration);
        hand.applyFingerBend();
    }

}
