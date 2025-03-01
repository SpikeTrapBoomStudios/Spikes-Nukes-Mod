package com.stbstudios.spikesnukes.math;

import java.util.Random;

public class Math2 {
    public static double getRandomNumber(double min, double max) {
        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }
}
