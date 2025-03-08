package com.stbstudios.spikesnukes.math;

import java.util.Random;

public class Math2 {
    public static double getRandomNumber(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    public static float interpolateColor(float start, float end, float progress) {
        return (start + progress * (end - start)) / 255f;
    }

    public static Vector2D rotateCoordinate2D(double x, double y, double angle) {
        double rotatedX = x * Math.cos(angle) - y * Math.sin(angle);
        double rotatedY = x * Math.sin(angle) + y * Math.cos(angle);
        return new Vector2D(rotatedX, rotatedY);
    }
}

