package io.github.dantetam.opstrykontest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Dante on 6/25/2016.
 */
public class ColorTextureHelper {

    private static Context context;

    public static void init(Context c) {
        context = c;
    }

    public static int loadColor(int r, int g, int b, int a) {
        Bitmap bitmap = createColor(r, g, b, a);
        return TextureHelper.loadTexture("rgba/" + intFromColor(r, g, b, a), bitmap);
    }

    public static int loadColor(float[] color) {
        if (
                color[0] > 0 && color[0] <= 1 &&
                color[1] > 0 && color[1] <= 1 &&
                color[2] > 0 && color[2] <= 1) {
            if (color.length == 3) {
                color = new float[]{color[0], color[1], color[2], 1.0f};
            }
            return loadColor((int) (color[0] * 255f), (int) (color[1] * 255f), (int) (color[2] * 255f), (int) (color[3] * 255f));
        }
        else if (color.length == 3) {
            color = new float[]{color[0], color[1], color[2], 255f};
        }
        return loadColor((int)color[0], (int)color[1], (int)color[2], (int)color[3]);
    }
    public static Bitmap createColor(int r, int g, int b, int a) {
        int rgba = intFromColor(r, g, b, a);
        return createColor(rgba);
    }
    public static Bitmap createColor(int rgba) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;	// No pre-scaling
        options.inMutable = true;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.usb_android, options);

        for (int y = 0; y < bitmap.getHeight(); y++){
            for (int x = 0; x < bitmap.getWidth(); x++){
                bitmap.setPixel(x,y,rgba);
            }
        }
        //Bitmap bitMap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        return bitmap;
    }

    private static int intFromColor(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}
