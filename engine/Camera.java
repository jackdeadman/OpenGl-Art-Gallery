package engine;

import gmaths.*;
import java.awt.event.*;
import com.jogamp.opengl.awt.GLCanvas;

/**
* Taken from the labsheet
*/

public class Camera {

  public enum CameraType {X, Z, Y};
  public enum Movement {NO_MOVEMENT, LEFT, RIGHT, UP, DOWN, FORWARD, BACK};

  private static final float DEFAULT_RADIUS = 25;
  public static final Vec3 DEFAULT_POSITION = new Vec3(0f,6f,16f);
  public static final Vec3 DEFAULT_POSITION_2 = new Vec3(15,10,0);
  public static final Vec3 DEFAULT_POSITION_3 = new Vec3(0,25,0);
  public static final Vec3 DEFAULT_TARGET = new Vec3(0,3,0);
  public static final Vec3 DEFAULT_UP = new Vec3(0,1,0);
  public static final Vec3 DEFAULT_DOWN = new Vec3(0,1,0);

  public final float YAW = -90f;
  public final float PITCH = 0f;
  public final float KEYBOARD_SPEED = 0.2f;
  public final float MOUSE_SPEED = 1.0f;

  private Vec3 position;
  private Vec3 target;
  private Vec3 up;
  private Vec3 worldUp;
  private Vec3 front;
  private Vec3 right;

  private float yaw;
  private float pitch;

  public Camera(Vec3 position, Vec3 target, Vec3 up) {
    setupCamera(position, target, up);
  }

  private void setupCamera(Vec3 position, Vec3 target, Vec3 up) {
    this.position = new Vec3(position);
    this.target = new Vec3(target);
    this.up = new Vec3(up);
    front = Vec3.subtract(target, position);
    front.normalize();
    up.normalize();
    calculateYawPitch(front);
    worldUp = new Vec3(up);
    updateCameraVectors();
  }

  public Vec3 getPosition() {
    return new Vec3(position);
  }

  public void setCamera(CameraType c) {
    switch (c) {
      case X : setupCamera(DEFAULT_POSITION, DEFAULT_TARGET, DEFAULT_UP) ; break;
      case Z : setupCamera(DEFAULT_POSITION_2, DEFAULT_TARGET, DEFAULT_UP); break;
      case Y : setupCamera(DEFAULT_POSITION_3, new Vec3(0.01f,2f,0.0f), DEFAULT_DOWN); break;
    }
  }

  private void calculateYawPitch(Vec3 v) {
    yaw = (float)Math.atan2(v.z,v.x);
    pitch = (float)Math.asin(v.y);
  }

  public Mat4 getViewMatrix() {
    target = Vec3.add(position, front);
    return Mat4Transform.lookAt(position, target, up);
  }


  // @author Jack Deadman
  // Addition to the library to calculate a view matrix which works for a skybox
  public Mat4 getSkyboxViewMatrix() {
    target = Vec3.add(new Vec3(0.0f, 0.0f, 0.0f), front);
    return Mat4Transform.lookAt(new Vec3(0.0f, 0.0f, 0.0f), target, up);
  }

  public void keyboardInput(Movement movement) {
    switch (movement) {
      case NO_MOVEMENT: break;
      case LEFT: position.add(Vec3.multiply(right, -KEYBOARD_SPEED)); break;
      case RIGHT: position.add(Vec3.multiply(right, KEYBOARD_SPEED)); break;
      case UP: position.add(Vec3.multiply(up, KEYBOARD_SPEED)); break;
      case DOWN: position.add(Vec3.multiply(up, -KEYBOARD_SPEED)); break;
      case FORWARD: position.add(Vec3.multiply(front, KEYBOARD_SPEED)); break;
      case BACK: position.add(Vec3.multiply(front, -KEYBOARD_SPEED)); break;
    }
  }

  public void updateYawPitch(float y, float p) {
    yaw += y;
    pitch += p;
    if (pitch > 89) pitch = 89;
    else if (pitch < -89) pitch = -89;
    updateFront();
    updateCameraVectors();
  }

  private void updateFront() {
    double cy, cp, sy, sp;
    cy = Math.cos(yaw);
    sy = Math.sin(yaw);
    cp = Math.cos(pitch);
    sp = Math.sin(pitch);
    front.x = (float)(cy*cp);
    front.y = (float)(sp);
    front.z = (float)(sy*cp);
    front.normalize();
    target = Vec3.add(position,front);
  }

  private void updateCameraVectors() {
    right = Vec3.crossProduct(front, worldUp);
    right.normalize();
    up = Vec3.crossProduct(right, front);
    up.normalize();
  }

}
