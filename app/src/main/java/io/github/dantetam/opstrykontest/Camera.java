package io.github.dantetam.opstrykontest;

import android.opengl.Matrix;

/**
 * Created by Dante on 6/12/2016.
 */
public class Camera {

    private final float[] viewMatrix = new float[16];
    public float eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ;

    public Camera() {
        upX = 0; upY = 1; upZ = 0;
    }

    public float[] getViewMatrix() {
        return viewMatrix;
    }

    private void setViewMatrix() {
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void moveTo(float a, float b, float c) {
        eyeX = a; eyeY = b; eyeZ = c;
        setViewMatrix();
    }

    public void moveShift(float a, float b, float c) {
        eyeX += a; eyeY += b; eyeZ += c;
        setViewMatrix();
    }

    public void pointTo(float a, float b, float c) {
        lookX = a; lookY = b; lookZ = c;
        setViewMatrix();
    }

    public void pointShift(float a, float b, float c) {
        lookX += a; lookY += b; lookZ += c;
        setViewMatrix();
    }

    public void orient(float a, float b, float c) {
        upX = a; upY = b; upZ = c;
        setViewMatrix();
    }

}
