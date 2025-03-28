package com.example.sae.core.quadtree;

/**
 * define the boundary of an element
 * @see QuadTree
 */
public class Boundary {
    /// boundary x starting coordinate
    protected double x;
    /// boundary y starting coordinate
    protected double y;
    /// boundary width
    protected double width;
    /// boundary height
    protected double height;

    /**
     * @param x the boundary origin x coordinate
     * @param y the boundary origin y coordinate
     * @param width the boundary width
     * @param height the boundary height
     */
    public Boundary(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * check if a point is contained in the boundary
     * @param px the target x coordinate
     * @param py the target y coordinate
     * @return true if the point is contained within the boundary, false otherwise
     */
    public boolean contains(double px, double py) {
        return (px >= x - width &&
                px <= x + width &&
                py >= y - height &&
                py <= y + height);
    }

    /**
     * check if the boundary intersect with another boundary
     * @param range the other boundary
     * @return true if there is an intersection, false otherwise
     */
    public boolean intersects(Boundary range) {
        return !(range.x - range.width > x + width ||
                range.x + range.width < x - width ||
                range.y - range.height > y + height ||
                range.y + range.height < y - height);
    }

    /**
     * {@return the x origin coordinate of the boundary}
     */
    public double getX() {
        return x;
    }

    /**
     * {@return the y origin coordinate of the boundary}
     */
    public double getY() {
        return y;
    }

    /**
     * {@return the width of the boundary}
     */
    public double getWidth() {
        return width;
    }

    /**
     * {@return the height of the boundary}
     */
    public double getHeight() {
        return height;
    }

}
