package com.ahui.lessonwms.utils;

public class Utilities {

    /**
     * Bounds parameter to the range [0, 1]
     */
    public static float saturate(float a) {
        return boundToRange(a, 0, 1.0f);
    }

    /**
     * @see #boundToRange(int, int, int).
     */
    public static float boundToRange(float value, float lowerBound, float upperBound) {
        return Math.max(lowerBound, Math.min(value, upperBound));
    }

    public static float mapRange(float value, float min, float max) {
        return min + (value * (max - min));
    }
}
