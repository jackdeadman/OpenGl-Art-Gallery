package meshes;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import engine.*;
import engine.PointLight;
import gmaths.*;
import java.nio.*;
import java.util.ArrayList;

public class CubeNew extends Mesh {
  private Material material;

  public Cube(GL3 gl, ShaderProgram program) {
    super(gl, program);
    super.vertices = this.vertices;
    super.indices = this.indices;

    fillBuffers(gl);
  }

  public void render(GL3 gl, Mat4 model) {

    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  public void dispose(GL3 gl) {
    super.dispose(gl);
    gl.glDeleteBuffers(1, textureId1, 0);
    gl.glDeleteBuffers(1, textureId2, 0);
  }

  // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering

   private float[] vertices = new float[] {  // x,y,z, nx,ny,nz, s,t
      -0.5f, -0.5f, -0.5f,  -1, 0, 0,  0.0f, 0.0f,  // 0
      -0.5f, -0.5f,  0.5f,  -1, 0, 0,  1.0f, 0.0f,  // 1
      -0.5f,  0.5f, -0.5f,  -1, 0, 0,  0.0f, 1.0f,  // 2
      -0.5f,  0.5f,  0.5f,  -1, 0, 0,  1.0f, 1.0f,  // 3
       0.5f, -0.5f, -0.5f,   1, 0, 0,  1.0f, 0.0f,  // 4
       0.5f, -0.5f,  0.5f,   1, 0, 0,  0.0f, 0.0f,  // 5
       0.5f,  0.5f, -0.5f,   1, 0, 0,  1.0f, 1.0f,  // 6
       0.5f,  0.5f,  0.5f,   1, 0, 0,  0.0f, 1.0f,  // 7

      -0.5f, -0.5f, -0.5f,  0,0,-1,  1.0f, 0.0f,  // 8
      -0.5f, -0.5f,  0.5f,  0,0,1,   0.0f, 0.0f,  // 9
      -0.5f,  0.5f, -0.5f,  0,0,-1,  1.0f, 1.0f,  // 10
      -0.5f,  0.5f,  0.5f,  0,0,1,   0.0f, 1.0f,  // 11
       0.5f, -0.5f, -0.5f,  0,0,-1,  0.0f, 0.0f,  // 12
       0.5f, -0.5f,  0.5f,  0,0,1,   1.0f, 0.0f,  // 13
       0.5f,  0.5f, -0.5f,  0,0,-1,  0.0f, 1.0f,  // 14
       0.5f,  0.5f,  0.5f,  0,0,1,   1.0f, 1.0f,  // 15

      -0.5f, -0.5f, -0.5f,  0,-1,0,  0.0f, 0.0f,  // 16
      -0.5f, -0.5f,  0.5f,  0,-1,0,  0.0f, 1.0f,  // 17
      -0.5f,  0.5f, -0.5f,  0,1,0,   0.0f, 1.0f,  // 18
      -0.5f,  0.5f,  0.5f,  0,1,0,   0.0f, 0.0f,  // 19
       0.5f, -0.5f, -0.5f,  0,-1,0,  1.0f, 0.0f,  // 20
       0.5f, -0.5f,  0.5f,  0,-1,0,  1.0f, 1.0f,  // 21
       0.5f,  0.5f, -0.5f,  0,1,0,   1.0f, 1.0f,  // 22
       0.5f,  0.5f,  0.5f,  0,1,0,   1.0f, 0.0f   // 23
   };

   private int[] indices =  new int[] {
      0,1,3, // x -ve
      3,2,0, // x -ve
      4,6,7, // x +ve
      7,5,4, // x +ve
      9,13,15, // z +ve
      15,11,9, // z +ve
      8,10,14, // z -ve
      14,12,8, // z -ve
      16,20,21, // y -ve
      21,17,16, // y -ve
      23,22,18, // y +ve
      18,19,23  // y +ve
  };

}
