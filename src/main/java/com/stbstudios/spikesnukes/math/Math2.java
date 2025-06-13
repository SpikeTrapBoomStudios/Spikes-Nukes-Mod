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

    public static float lerp(float start, float end, float amount) {
        return start + (end - start) * amount;
    }

    public static double lerpWithEaseIn(double start, double end, double amount) {
        double easedValue;
        if (amount < 1) {
            easedValue = Math.pow(amount, 3);
        } else {
            easedValue = 1;
        }
        return start + (end - start) * easedValue;
    }
}

