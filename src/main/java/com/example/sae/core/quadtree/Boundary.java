package com.example.sae.core.quadtree;

import com.example.sae.core.entity.MoveableBody;
import com.example.sae.core.entity.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.core.entity.enemyStrategy.EnemyStrategy;
import com.example.sae.core.entity.enemyStrategy.RandomMoveStrategy;
import com.example.sae.core.entity.enemyStrategy.SeekFoodStrategy;
import  com.example.sae.core.Camera;

/**
 * a kind of box that delimits a small part of the game
 *
 * @see QuadTree
 * @see Camera
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class Boundary {

    /**
     * Position coordinates abscissa
     */
    protected double x;

    /**
     * Position coordinates ordinate
     */
    protected double y;

    /**
     *  width dimension
     */
    protected double w;

    /**
     *  height dimension
     */
    protected double h;

    /**
     * constructor
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param x position coordinates abscissa
     * @param y position coordinates ordinate
     * @param w width dimension
     * @param h height dimension
     */
    public Boundary(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     * Checks if a point is inside this boundary
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param px position coordinates abscissa of point
     * @param py position coordinates ordinate of point
     * @return returns true if the point lies within this boundary
     */
    public boolean contains(double px, double py) {
        return (px >= x - w &&
                px <= x + w &&
                py >= y - h &&
                py <= y + h);
    }

    /**
     * Check if this border and another border intersect
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param range other boundary
     * @return returns true if the point lies within this boundary
     */
    public boolean intersects(Boundary range) {
        return !(range.x - range.w > x + w ||
                range.x + range.w < x - w ||
                range.y - range.h > y + h ||
                range.y + range.h < y - h);
    }

    // Getters and setters

    /**
     * returns Position coordinates abscissa
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns Position coordinates abscissa
     */
    public double getX() { return x; }

    /**
     * returns Position coordinates ordinate
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns Position coordinates ordinate
     */
    public double getY() { return y; }

    /**
     * returns width dimension
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns width dimension
     */
    public double getWidth() { return w; }

    /**
     * returns height dimension
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns height dimension
     */
    public double getHeight() { return h; }

}
