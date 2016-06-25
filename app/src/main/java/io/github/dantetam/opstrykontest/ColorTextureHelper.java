package io.github.dantetam.opstrykontest;

import android.graphics.Bitmap;

/**
 * Created by Dante on 6/25/2016.
 */
public class ColorTextureHelper {

    public static int loadColor(int width, int height, int r, int g, int b, int a) {
        Bitmap bitmap = createColor(width, height, r, g, b, a);
        return TextureHelper.loadTexture("rgba/" + width + "/" + height + "/" + intFromColor(r, g, b, a), bitmap);
    }

    public static int loadColor(int width, int height, float[] color) {
        if (
                color[0] > 0 && color[0] <= 1 &&
                color[1] > 0 && color[1] <= 1 &&
                color[2] > 0 && color[2] <= 1 &&
                color[3] > 0 && color[3] <= 1) {
            return loadColor(width, height, (int) (color[0] * 255), (int) (color[1] * 255), (int) (color[2] * 255), (int) (color[3] * 255));
        }
        else
            return loadColor(width, height, (int)color[0], (int)color[1], (int)color[2], (int)color[3]);
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

    private static int intFromColor(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
