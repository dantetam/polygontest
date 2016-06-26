package io.github.dantetam.opstrykontest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.util.HashMap;

public class TextureHelper
{
    public static HashMap<String, Integer> texturesByName = new HashMap<>();

    public static int loadTexture(final String name) {
        if (texturesByName.containsKey(name)) {
            return texturesByName.get(name);
        }
        return -1;
    }
	public static int loadTexture(final String name, final Context context, final int resourceId)
	{
        if (texturesByName.containsKey(name)) {
            return texturesByName.get(name);
        }
		final int[] textureHandle = new int[1];
		GLES20.glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;	// No pre-scaling
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
			bindBitmap(bitmap, textureHandle[0]);
		}
		else {
			throw new RuntimeException("Error loading texture.");
		}

        texturesByName.put(name, textureHandle[0]);
		return textureHandle[0];
	}

    public static int loadTexture(final String name, final Bitmap bitmap) {
        if (texturesByName.containsKey(name)) {
            return texturesByName.get(name);
        }
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;	// No pre-scaling
            bindBitmap(bitmap, textureHandle[0]);
        }
        else {
            throw new RuntimeException("Error loading texture.");
        }

        texturesByName.put(name, textureHandle[0]);
        return textureHandle[0];
    }

    public static void bindBitmap(Bitmap bitmap, int textureHandle) {
        // Bind to the texture in OpenGL
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle();
    }
}
