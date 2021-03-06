package engine.render;

/**
 * Mainly the same as the one from the lab class.
 */

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

import gmaths.*;
import engine.*;

public abstract class Mesh {
    protected float[] vertices;
    protected int[] indices;
    private int vertexStride = 8;
    private int vertexXYZFloats = 3;
    private int vertexNormalFloats = 3;
    private int vertexTexFloats = 2;
    private int[] vertexBufferId = new int[1];
    protected int[] vertexArrayId = new int[1];
    private int[] elementBufferId = new int[1];

    protected Material material;
    protected Shader shader;
    protected Mat4 model;

    protected Mat4 perspective;
    protected WorldConfiguration worldConfig;
    protected ShaderConfigurator program;

    public Mesh(GL3 gl) {
        material = new Material();
        model = new Mat4(1);
    }

    public Mesh(GL3 gl, ShaderConfigurator program) {
        material = new Material();
        model = new Mat4(1);
        this.program = program;
    }

    public ShaderConfigurator getShaderProgram() {
        return program;
    }
    protected void setWorldConfig(WorldConfiguration worldConfig) {
        this.worldConfig = worldConfig;
    }

    public void setModelMatrix(Mat4 m) {
        model = m;
    }

    public void setPerspective(Mat4 perspective) {
        this.perspective = perspective;
    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, vertexBufferId, 0);
        gl.glDeleteVertexArrays(1, vertexArrayId, 0);
        gl.glDeleteBuffers(1, elementBufferId, 0);
        // Need to dispose the shader program as well e.g textures
        if (program != null)
            program.dispose(gl);
    }

    public void fillBuffers(GL3 gl) {
        gl.glGenVertexArrays(1, vertexArrayId, 0);
        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glGenBuffers(1, vertexBufferId, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);

        FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);

        int stride = vertexStride;
        int numXYZFloats = vertexXYZFloats;
        int offset = 0;

        gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
        gl.glEnableVertexAttribArray(0);

        int numNormalFloats = vertexNormalFloats; // x,y,z for each vertex
        offset = numXYZFloats*Float.BYTES;  // the normal values are three floats after the three x,y,z values
                                    // so change the offset value
        gl.glVertexAttribPointer(1, numNormalFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
                                    // the vertex shader uses location 1 (sometimes called index 1)
                                    // for the normal information
                                    // location, size, type, normalize, stride, offset
                                    // offset is relative to the start of the array of data
        gl.glEnableVertexAttribArray(1);// Enable the vertex attribute array at location 1

        // now do the texture coordinates  in vertex attribute 2
        int numTexFloats = vertexTexFloats;
        offset = (numXYZFloats+numNormalFloats)*Float.BYTES;
        gl.glVertexAttribPointer(2, numTexFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
        gl.glEnableVertexAttribArray(2);

        gl.glGenBuffers(1, elementBufferId, 0);
        IntBuffer ib = Buffers.newDirectIntBuffer(indices);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
        gl.glBindVertexArray(0);
    }

    public void render(GL3 gl, Mat4 model) {
        program.sendSendDataToTheGPU(gl, perspective, model, worldConfig);

        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }

    public void render(GL3 gl) {
        render(gl, model);
    }

}
