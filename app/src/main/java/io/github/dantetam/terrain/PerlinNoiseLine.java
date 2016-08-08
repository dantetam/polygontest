package io.github.dantetam.terrain;

import java.util.Random;

/**
 * Created by Dante on 8/6/2016.
 */
public class PerlinNoiseLine {

    public Random random;

    public PerlinNoiseLine(long seed) {
        random = new Random(seed);
    }

    public float[] generatePerlinLine(int len, float persistence, float amp) {
        int iterations = 4;
        int decreasingWidth = len/4;
        float[] result = new float[len];
        for (int i = 0; i < iterations; i++) {
            float[] noise = generateNoise(len, decreasingWidth, amp);
            for (int j = 0; j < noise.length; j++) {
                result[j] += noise[j];
            }
            if (decreasingWidth <= 1) {
                break;
            }
            amp /= 2;
            decreasingWidth /= 2;
        }
        return result;
    }

    private float[] generateNoise(int len, int patchLen, float amp) {
        float[] result = new float[len];
        for (int i = 0; i < len; i += patchLen) {
            float patchHeight = random.nextFloat()*amp*2 - amp;
            for (int j = i; j < i + patchLen; j++) {
                if (j >= len) return result;
                result[j] = patchHeight;
            }
        }
        return result;
    }

}
