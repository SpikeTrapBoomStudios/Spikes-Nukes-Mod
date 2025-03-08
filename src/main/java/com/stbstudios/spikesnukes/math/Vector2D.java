package com.stbstudios.spikesnukes.math;

public class Vector2D {
    public double x;
    public double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D offset(double offsetX, double offsetY) {
        this.x += offsetX;
        this.y += offsetY;
        return this;
    }
}
