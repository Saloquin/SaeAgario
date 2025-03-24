package com.example.sae.core.quadtree;

import com.example.sae.entity.Entity;
import java.util.ArrayList;
import java.util.List;

public class QuadTree {
    private Boundary boundary;
    private int capacity;
    private List<Entity> entities;
    private boolean divided;

    private QuadTree northwest;
    private QuadTree northeast;
    private QuadTree southwest;
    private QuadTree southeast;

    private int maxDepth;
    private int currentDepth;

    public QuadTree(Boundary boundary, int capacity, int maxDepth) {
        this.boundary = boundary;
        this.capacity = capacity;
        this.entities = new ArrayList<>();
        this.divided = false;
        this.maxDepth = maxDepth;
        this.currentDepth = 0;
    }

    private QuadTree(Boundary boundary, int capacity, int maxDepth, int currentDepth) {
        this(boundary, capacity, maxDepth);
        this.currentDepth = currentDepth;
    }

    public void subdivide() {
        double x = boundary.getX();
        double y = boundary.getY();
        double w = boundary.getWidth() / 2;
        double h = boundary.getHeight() / 2;

        Boundary nw = new Boundary(x - w / 2, y - h / 2, w, h);
        northwest = new QuadTree(nw, capacity, maxDepth, currentDepth + 1);

        Boundary ne = new Boundary(x + w / 2, y - h / 2, w, h);
        northeast = new QuadTree(ne, capacity, maxDepth, currentDepth + 1);

        Boundary sw = new Boundary(x - w / 2, y + h / 2, w, h);
        southwest = new QuadTree(sw, capacity, maxDepth, currentDepth + 1);

        Boundary se = new Boundary(x + w / 2, y + h / 2, w, h);
        southeast = new QuadTree(se, capacity, maxDepth, currentDepth + 1);

        divided = true;
    }

    public boolean insert(Entity entity) {
        if (!boundary.contains(entity.Sprite.getCenterX(), entity.Sprite.getCenterY())) {
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
            for (Entity e : entities) {
                insertIntoChildren(e);
            }
            entities.clear();
        }

        return insertIntoChildren(entity);
    }

    private boolean insertIntoChildren(Entity entity) {
        return northwest.insert(entity) ||
                northeast.insert(entity) ||
                southwest.insert(entity) ||
                southeast.insert(entity);
    }

    public List<Entity> query(Boundary range) {
        List<Entity> found = new ArrayList<>();

        if (!boundary.intersects(range)) {
            return found;
        }

        for (Entity entity : entities) {
            if (range.contains(entity.Sprite.getCenterX(), entity.Sprite.getCenterY())) {
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