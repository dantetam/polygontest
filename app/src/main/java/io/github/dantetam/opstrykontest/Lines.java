package io.github.dantetam.opstrykontest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Dante on 6/20/2016.
 */
public class Lines {
    public int mCubeBufferIdx = -1;

    /** This will be used to pass in model position information. */
    public int mPositionHandle;

    /** This will be used to pass in model normal information. */
    public int mNormalHandle;

    /** This will be used to pass in model texture coordinate information. */
    public int mTextureCoordinateHandle;

    public int textureHandle;

    /** Size of the position data in elements. */
    static final int POSITION_DATA_SIZE = 3;

    /** Size of the normal data in elements. */
    static final int NORMAL_DATA_SIZE = 3;

    /** Size of the texture coordinate data in elements. */
    static final int TEXTURE_COORDINATE_DATA_SIZE = 2;

    /** How many bytes per float. */
    static final int BYTES_PER_FLOAT = 4;

    public final float[] width = new float[3];
    public final float[] color = new float[4];

    public Lines(int textureHandle, float[] lineVertices, float[] lineNormals, float[] lineTextures) {
        this.textureHandle = textureHandle;

        FloatBuffer cubeBuffer = getInterleavedBuffer(lineVertices, lineNormals, lineTextures);

        numVerticesToRender = lineVertices.length;

        // Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, cubeBuffer.capacity() * BYTES_PER_FLOAT, cubeBuffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        mCubeBufferIdx = buffers[0];

        cubeBuffer.limit(0);
        cubeBuffer = null;
    }

    FloatBuffer getInterleavedBuffer(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates) {
        final int cubeDataLength = cubePositions.length
                + (cubeNormals.length)
                + (cubeTextureCoordinates.length);
        int cubePositionOffset = 0;
        int cubeNormalOffset = 0;
        int cubeTextureOffset = 0;

        final FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(cubeDataLength * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int v = 0; v < cubePositions.length / POSITION_DATA_SIZE; v++) {
            //float[] result = new float[cubeBuffer.limit()];
            //cubeBuffer.get(result);
            //System.out.println(">>> " + cubePositions.length + " " + cubePositionOffset + " " + cubeDataLength);
            cubeBuffer.put(cubePositions, cubePositionOffset, POSITION_DATA_SIZE);
            cubePositionOffset += POSITION_DATA_SIZE;
            cubeBuffer.put(cubeNormals, cubeNormalOffset, NORMAL_DATA_SIZE);
            cubeNormalOffset += NORMAL_DATA_SIZE;
            cubeBuffer.put(cubeTextureCoordinates, cubeTextureOffset, TEXTURE_COORDINATE_DATA_SIZE);
            cubeTextureOffset += TEXTURE_COORDINATE_DATA_SIZE;
        }

        cubeBuffer.position(0);

        return cubeBuffer;
    }

    public int numVerticesToRender;
    public void renderAll() {
        final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;

        // Pass in the position information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

        // Pass in the normal information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, POSITION_DATA_SIZE * BYTES_PER_FLOAT);

        // Pass in the texture information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
                stride, (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * BYTES_PER_FLOAT);

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Draw the cubes.
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, numVerticesToRender); //36 vertices in a cube, of size 3 (GLES20.GL_TRIANGLES)
    }

    public void render(int blockIndex) {
        final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + TEXTURE_COORDINATE_DATA_SIZE) * BYTES_PER_FLOAT;

        // Pass in the position information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, 0);

        // Pass in the normal information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
        GLES20.glEnableVertexAttribArray(mNormalHandle);
        GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, POSITION_DATA_SIZE * BYTES_PER_FLOAT);

        // Pass in the texture information
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mCubeBufferIdx);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, TEXTURE_COORDINATE_DATA_SIZE, GLES20.GL_FLOAT, false,
                stride, (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * BYTES_PER_FLOAT);

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Draw the cubes.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, blockIndex*36, 36); //36 vertices in a cube, of size 3 (GLES20.GL_TRIANGLES)
    }

    public void release() {
        // Delete buffers from OpenGL's memory
        final int[] buffersToDelete = new int[] { mCubeBufferIdx };
        GLES20.glDeleteBuffers(buffersToDelete.length, buffersToDelete, 0);
    }

    public void color(float[] t) {
        if (t.length == 3)
            color(t[0], t[1], t[2], 1.0f);
        else if (t.length == 4)
            color(t[0], t[1], t[2], t[3]);
        else
            throw new IllegalArgumentException("Color argument is not of correct length");
    }
    public void color(float a, float b, float c, float d) {
        color[0] = a; color[1] = b; color[2] = c; color[3] = d;
    }

}
