package com.example.sae.core.quadtree;

import com.example.sae.core.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * segment a plan in subarea
 */
public class QuadTree {
    /// the quadtree boundary
    private final Boundary boundary;
    /// the quadtree capacity
    private int capacity;
    /// the list of entities in the quadtree area
    private final HashSet<Entity> entities;
    /// if the quadtree is divided
    private boolean divided;

    /// the northwest subarea of this area
    private QuadTree northwest;
    /// the northeast subarea of this area
    private QuadTree northeast;
    /// the southeast subarea of this area
    private QuadTree southwest;
    /// the southeast subarea of this area
    private QuadTree southeast;

    /// the maximum depth level of the quadtree
    private final int maxDepth;
    /// the current depth level of this quadtree
    private int currentDepth;

    /**
     * @param boundary the boundary of the quadtree
     * @param maxDepth the maximum depth level
     */
    public QuadTree(Boundary boundary, int maxDepth) {
        this.boundary = boundary;
        this.entities = new HashSet<>();
        this.divided = false;
        this.maxDepth = maxDepth;
        this.currentDepth = 0;
    }

    /**
     * @param boundary the boundary of the quadtree
     * @param maxDepth the maximum depth level
     * @param currentDepth the current depth level
     */
    private QuadTree(Boundary boundary, int maxDepth, int currentDepth) {
        this(boundary, maxDepth);
        this.currentDepth = currentDepth;
    }

    /**
     * segment the current quadtree node in 4 quadtree
     */
    public void subdivide() {
        double x = boundary.getX();
        double y = boundary.getY();
        double w = boundary.getWidth();
        double h = boundary.getHeight();

        Boundary nw = new Boundary(x - w / 2, y - h / 2, w / 2, h / 2);
        northwest = new QuadTree(nw, maxDepth, currentDepth + 1);

        Boundary ne = new Boundary(x + w / 2, y - h / 2, w / 2, h / 2);
        northeast = new QuadTree(ne, maxDepth, currentDepth + 1);

        Boundary sw = new Boundary(x - w / 2, y + h / 2, w / 2, h / 2);
        southwest = new QuadTree(sw, maxDepth, currentDepth + 1);

        Boundary se = new Boundary(x + w / 2, y + h / 2, w / 2, h / 2);
        southeast = new QuadTree(se, maxDepth, currentDepth + 1);

        divided = true;
    }

    /**
     * insert an entity in the quadtree entities list
     * @param entity the entity to add
     * @return true if the insertion is successful, false otherwise
     */
    public boolean insert(Entity entity) {
        if (!boundary.contains(entity.getSprite().getCenterX(), entity.getSprite().getCenterY())) {
            return false;
        }

        if (currentDepth == maxDepth) {
            entities.add(entity);
            return true;
        }

        if (entities.size() < capacity) {
            entities.add(entity);
            return true;
        }

        if (!divided) {
            subdivide();
        }

        return insertIntoChildren(entity);
    }

    /**
     * remove an entity from the quadtree entities list
     * @param entity the entity to remove
     * @return true if the deletion was successful, false otherwise
     */
    public boolean remove(Entity entity) {
        // if the entity isn't in the quadtree area, do nothing
        if (!boundary.contains(entity.getSprite().getCenterX(), entity.getSprite().getCenterY())) {
            return false;
        }

        if (currentDepth == maxDepth) {
            return entities.remove(entity);
        }

        if (entities.contains(entity)) {
            entities.remove(entity);
            return true;
        }

        if (divided) {
            boolean removed = false;

            removed |= northwest.remove(entity);
            removed |= northeast.remove(entity);
            removed |= southwest.remove(entity);
            removed |= southeast.remove(entity);

            return removed;
        }

        return false;
    }

    /**
     * insert an entity in the sub-quadtree
     * @param entity the entity to insert
     * @return true if the insertion was successful, false otherwise
     */
    private boolean insertIntoChildren(Entity entity) {
        if (northwest.insert(entity)) return true;
        else if (northeast.insert(entity)) return true;
        else if (southwest.insert(entity)) return true;
        else return southeast.insert(entity);
    }

    /**
     * get the entities within an area
     * @param range the boundary of the search
     * @return the entities list
     */
    public List<Entity> query(Boundary range) {
        List<Entity> found = new ArrayList<>();

        if (!boundary.intersects(range)) {
            return found;
        }

        for (Entity entity : entities) {
            if (range.contains(entity.getSprite().getCenterX(), entity.getSprite().getCenterY())) {
                found.add(entity);
            }
        }

        if (divided) {
            found.addAll(northwest.query(range));
            found.addAll(northeast.query(range));
            found.addAll(southwest.query(range));
            found.addAll(southeast.query(range));
        }

        return found;
    }
}