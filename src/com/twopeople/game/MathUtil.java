package com.twopeople.game;

/**
 * Created by Alexey
 * At 7:55 PM on 2/22/14
 */

public class MathUtil {
    public static float getDist(float x1, float y1, float z1, float x2, float y2, float z2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
    }

    public static float getDist(float x1, float y1, float x2, float y2) {
        return getDist(x1, y1, 0, x2, y2, 0);
    }
}