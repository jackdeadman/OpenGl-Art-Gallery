package models;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import engine.*;
import engine.WorldConfiguration;
import gmaths.*;
import meshes.*;

public class Room extends Model {

    private Mesh floor, back, left, right, roof, front;
    private float floorWidth = 16;
    private float floorLength = 12;
    private float ceilingHeight = 10;
    private NameNode floorName;
    private int[] floorTexture;
    private int[] containerTexture;
    private int[] windowTexture;
    private int[] ceilingTexture;
    private int[] window2;
    private int[] backWallTexture;
    private int[] backWallNormal;
    private int[] windowNormal;

    private TransformNode moveLeftWall;

    public Room(WorldConfiguration worldConfig, int floorWidth, int floorLength, int ceilingHeight) {
        super(worldConfig);
        this.floorWidth = floorWidth;
        this.floorLength = floorLength;
        this.ceilingHeight = ceilingHeight;
    }

    protected void start(GL3 gl) {
        loadTextures(gl);
        loadMeshes(gl);
        buildSceneGraph();
    }

    public void addPictureToLeftWall(PictureFrame picture) {
        TransformNode nudge = new TransformNode("", Mat4Transform.translate(0.0f, 0.1f, 0.0f));
        moveLeftWall.addChild(nudge);
            nudge.addChild(picture.getRoot());
        getRoot().update();
    }

    public void setLeftWallPhotos() {

    }

    private void loadTextures(GL3 gl) {
        floorTexture = TextureLibrary.loadTexture(gl, "textures/wood_floor.jpg");
        containerTexture = TextureLibrary.loadTexture(gl, "textures/wallpaper.jpg");
        windowTexture = TextureLibrary.loadTexture(gl, "textures/window_black.jpg");
        ceilingTexture = TextureLibrary.loadTexture(gl, "textures/floor_3.jpg");
        window2 = TextureLibrary.loadTexture(gl, "textures/window2.jpg");
        backWallTexture = TextureLibrary.loadTexture(gl, "textures/wall_wood.jpg");
        backWallNormal = TextureLibrary.loadTexture(gl, "textures/wall_wood_normal.jpg");
        windowNormal = TextureLibrary.loadTexture(gl, "textures/window_black_normal.jpg");
    }

    private void loadMeshes(GL3 gl) {
        // make meshes
        floor = new TwoTriangles(gl, floorTexture);
        back = new TwoTriangles3(gl, backWallTexture, backWallTexture, window2, backWallNormal);
        left = new TwoTriangles2(gl, windowTexture, containerTexture);
        right = new TwoTriangles2(gl, windowTexture, containerTexture);
        roof = new TwoTriangles(gl, containerTexture);
        front = new TwoTriangles2(gl, windowTexture, containerTexture);

        registerMeshes(new Mesh[] { floor, back, left, right, roof, front });
    }

    private void buildSceneGraph() {

        TransformNode floorTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4Transform.scale(floorWidth, 1, floorLength)
        );

        TransformNode backTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0, ceilingHeight / 2.0f, -floorLength / 2.0f),
                Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                Mat4Transform.rotateAroundX(90.0f)
            )
        );

        TransformNode scaleLeftWall = new TransformNode("", Mat4Transform.scale(floorLength, 1, ceilingHeight));

        moveLeftWall = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                Mat4Transform.translate(-floorWidth/2.0f, ceilingHeight / 2.0f, 0),
                Mat4Transform.rotateAroundZ(-90.0f),
                Mat4Transform.rotateAroundY(90.0f)
                // Mat4Transform.rotateAroundZ(10.0f)
            )
        );

        TransformNode rightTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                // Mat4Transform.translate(0, ceilingHeight / 2.0f, -floorLength / 2.0f),
                // Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                Mat4Transform.scale(1, ceilingHeight, floorLength),
                Mat4Transform.translate(floorWidth/2.0f, 0.5f, 0),
                Mat4Transform.rotateAroundZ(90.0f),
                Mat4Transform.rotateAroundY(-90.0f)
                // Mat4Transform.rotateAroundZ(10.0f)
            )
        );

        TransformNode roofTransform = new TransformNode(
            "Scale(16, 1, 16)",
            Mat4.multiplyVariable(
                // Mat4Transform.translate(0, ceilingHeight / 2.0f, -floorLength / 2.0f),
                // Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                Mat4Transform.scale(floorWidth, 1, floorLength),
                Mat4Transform.translate(0.0f, ceilingHeight, 0),
                Mat4Transform.rotateAroundX(180.0f)
            )
        );

        TransformNode frontTransform = new TransformNode(
            "",
            Mat4.multiplyVariable(
                Mat4Transform.translate(0, ceilingHeight / 2.0f, floorLength / 2.0f),
                // Mat4Transform.translate(0, 1.0f, 0),
                Mat4Transform.scale(floorWidth, ceilingHeight, 1),
                Mat4Transform.rotateAroundX(-90.0f)
            )
        );

        MeshNode floorShape = new MeshNode("TwoTriangles", floor);
        MeshNode backShape = new MeshNode("TwoTriangles", back);
        MeshNode leftShape = new MeshNode("TwoTriangles", left);
        MeshNode rightShape = new MeshNode("TwoTriangles", right);
        MeshNode roofShape = new MeshNode("TwoTriangles", roof);
        MeshNode frontShape = new MeshNode("TwoTriangles", front);

        floorName = new NameNode("Floor");

        SGNode root = new NameNode("Room");
        root.addChild(floorName);
            floorName.addChild(floorTransform);
                floorTransform.addChild(floorShape);
            floorName.addChild(backTransform);
                backTransform.addChild(backShape);
            floorName.addChild(moveLeftWall);
                moveLeftWall.addChild(scaleLeftWall);
                    scaleLeftWall.addChild(leftShape);
            floorName.addChild(rightTransform);
                rightTransform.addChild(rightShape);
            floorName.addChild(roofTransform);
                roofTransform.addChild(roofShape);
            floorName.addChild(frontTransform);
                frontTransform.addChild(frontShape);

        root.update();
        setRoot(root);
    }

    public SGNode getAnchor() {
        return floorName;
    }
}
