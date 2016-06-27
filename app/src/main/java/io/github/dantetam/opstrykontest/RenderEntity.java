package io.github.dantetam.opstrykontest;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Dante on 6/20/2016.
 */
public abstract class RenderEntity {

    /** Size of the position data in elements. */
    static final int POSITION_DATA_SIZE = 3;

    /** Size of the normal data in elements. */
    static final int NORMAL_DATA_SIZE = 3;

    /** Size of the texture coordinate data in elements. */
    static final int TEXTURE_COORDINATE_DATA_SIZE = 2;

    /** How many bytes per float. */
    static final int BYTES_PER_FLOAT = 4;

    /** This will be used to pass in model position information. */
    public int mPositionHandle;

    /** This will be used to pass in model normal information. */
    public int mNormalHandle;

    /** This will be used to pass in model texture coordinate information. */
    public int mTextureCoordinateHandle;

    public int textureHandle;

    public final int renderMode = GLES20.GL_TRIANGLES;

    abstract void renderAll();
    abstract void renderAll(int mode);
    abstract void render(int indexBlock);
    abstract void release();

   /* FloatBuffer[] getBuffers(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
        // First, copy cube information into client-side floating point buffers.
        final FloatBuffer cubePositionsBuffer;
        final FloatBuffer cubeNormalsBuffer;
        final FloatBuffer cubeTextureCoordinatesBuffer;

        cubePositionsBuffer = ByteBuffer.allocateDirect(cubePositions.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        cubePositionsBuffer.put(cubePositions).position(0);

        cubeNormalsBuffer = ByteBuffer.allocateDirect(cubeNormals.length * BYTES_PER_FLOAT * generatedCubeFactor * generatedCubeFactor)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor); i++) {
            cubeNormalsBuffer.put(cubeNormals);
        }

        cubeNormalsBuffer.position(0);

        cubeTextureCoordinatesBuffer = ByteBuffer.allocateDirect(cubeTextureCoordinates.length * BYTES_PER_FLOAT * generatedCubeFactor * generatedCubeFactor)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int i = 0; i < (generatedCubeFactor * generatedCubeFactor); i++) {
            cubeTextureCoordinatesBuffer.put(cubeTextureCoordinates);
        }

        cubeTextureCoordinatesBuffer.position(0);

        return new FloatBuffer[] {cubePositionsBuffer, cubeNormalsBuffer, cubeTextureCoordinatesBuffer};
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
    }*/

    FloatBuffer getInterleavedBuffer(float[] cubePositions, float[] cubeNormals, float[] cubeTextureCoordinates, int generatedCubeFactor) {
        final int cubeDataLength = cubePositions.length
                + (cubeNormals.length * generatedCubeFactor * generatedCubeFactor)
                + (cubeTextureCoordinates.length * generatedCubeFactor * generatedCubeFactor);
        int cubePositionOffset = 0;
        int cubeNormalOffset = 0;
        int cubeTextureOffset = 0;

        final FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(cubeDataLength * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (int i = 0; i < generatedCubeFactor * generatedCubeFactor; i++) {
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

            // The normal and texture data is repeated for each cube.
            cubeNormalOffset = 0;
            cubeTextureOffset = 0;
        }

        cubeBuffer.position(0);

        return cubeBuffer;
    }
}
