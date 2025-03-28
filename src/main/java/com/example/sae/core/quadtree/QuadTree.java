package com.example.sae.core.quadtree;

import com.example.sae.core.entity.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class QuadTree {
    private Boundary boundary;
    private int capacity;
    private HashSet<Entity> entities;
    private boolean divided;

    private QuadTree northwest;
    private QuadTree northeast;
    private QuadTree southwest;
    private QuadTree southeast;

    private int maxDepth;
    private int currentDepth;

    public QuadTree(Boundary boundary,  int maxDepth) {
        this.boundary = boundary;
        this.entities = new HashSet<>();
        this.divided = false;
        this.maxDepth = maxDepth;
        this.currentDepth = 0;
    }

    private QuadTree(Boundary boundary, int maxDepth, int currentDepth) {
        this(boundary, maxDepth);
        this.currentDepth = currentDepth;
    }



    public void subdivide() {
        double x = boundary.getX();
        double y = boundary.getY();
        double w = boundary.getWidth();
        double h = boundary.getHeight();

        Boundary nw = new Boundary(x - w / 2, y - h / 2, w /2, h /2);
        northwest = new QuadTree(nw, maxDepth, currentDepth + 1);

        Boundary ne = new Boundary(x + w / 2, y - h / 2, w/2, h/2);
        northeast = new QuadTree(ne, maxDepth, currentDepth + 1);

        Boundary sw = new Boundary(x - w / 2, y + h / 2, w/2, h/2);
        southwest = new QuadTree(sw, maxDepth, currentDepth + 1);

        Boundary se = new Boundary(x + w / 2, y + h / 2, w/2, h/2);
        southeast = new QuadTree(se, maxDepth, currentDepth + 1);

        divided = true;
    }

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

    public boolean remove(Entity entity) {
        // Si l'entité n'est pas dans la zone du QuadTree, ne rien faire
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


    private boolean insertIntoChildren(Entity entity) {
        if(northwest.insert(entity)) return true;
        else if(northeast.insert(entity)) return true;
        else if(southwest.insert(entity)) return true;
        else return southeast.insert(entity);
    }

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


    public void printAllEntities() {
        System.out.println("=== QuadTree Entities ===");
        int totalEntities = printEntitiesRecursive(0);
        System.out.println("Total entities: " + totalEntities);
        System.out.println("=== End of QuadTree ===");
    }

    private int printEntitiesRecursive(int depth) {
        String indent = "  ".repeat(depth);
        int totalCount = entities.size();

        // Print current node entities
        System.out.println(indent + "Node at depth " + depth + " (" +
                boundary.getX() + ", " + boundary.getY() + ") - " +
                "Size: " + entities.size());

        for (Entity entity : entities) {
            System.out.println(indent + "- " + entity.getClass().getSimpleName() +
                    " at [" + String.format("%.2f", entity.getSprite().getCenterX()) +
                    ", " + String.format("%.2f", entity.getSprite().getCenterY()) + "]");
        }

        // Recursively print child nodes
        if (divided) {
            System.out.println(indent + "Northwest:");
            totalCount += northwest.printEntitiesRecursive(depth + 1);
            System.out.println(indent + "Northeast:");
            totalCount += northeast.printEntitiesRecursive(depth + 1);
            System.out.println(indent + "Southwest:");
            totalCount += southwest.printEntitiesRecursive(depth + 1);
            System.out.println(indent + "Southeast:");
            totalCount += southeast.printEntitiesRecursive(depth + 1);
        }

        return totalCount;
    }
}