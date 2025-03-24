package com.example.sae.core.quadtree;

public class Boundary {
    // Position coordinates
    protected double x;
    protected double y;
    // Dimensions
    protected double w;
    protected double h;

    public Boundary(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // Check if a point is within this boundary
    public boolean contains(double px, double py) {
        return (px >= x - w &&
                px <= x + w &&
                py >= y - h &&
                py <= y + h);
    }

    // Check if this boundary intersects with another boundary
    public boolean intersects(Boundary range) {
        return !(range.x - range.w > x + w ||
                range.x + range.w < x - w ||
                range.y - range.h > y + h ||
                range.y + range.h < y - h);
    }

    // Getters and setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return w; }
    public double getHeight() { return h; }
}