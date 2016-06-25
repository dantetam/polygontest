package io.github.dantetam.opstrykontest;

import android.graphics.Bitmap;

/**
 * Created by Dante on 6/25/2016.
 */
public class ColorTextureHelper {

    public static int loadColor(int width, int height, int r, int g, int b, int a) {
        Bitmap bitmap = createColor(width, height, r, g, b, a);
        return TextureHelper.loadTexture(bitmap);
    }

    public static Bitmap createColor(int width, int height, int r, int g, int b, int a) {
        int rgba = (a << 24) | (r << 16) | (g << 8) | b;
        return createColor(width, height, rgba);
    }
    public static Bitmap createColor(int width, int height, int rgba) {
        int[] colors = new int[width*height];
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                colors[y*width + x] = rgba;
            }
        }
        Bitmap bitMap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        return bitMap;
    }

}
