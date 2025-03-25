package com.example.sae.core;

import com.example.sae.core.entity.*;
import com.example.sae.core.quadtree.Boundary;
import com.example.sae.core.quadtree.QuadTree;
import javafx.scene.shape.Shape;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameEngine {
    private final HashSet<Entity> entities;
    public final HashSet<Entity> entitiesMovable;
    private final WorldBounds worldBounds;

    private static final int QUAD_TREE_MAX_DEPTH = 6;
    private static QuadTree quadTree;

    public static final double MAP_LIMIT_WIDTH = 2000;
    public static final double MAP_LIMIT_HEIGHT = 2000;

    private final boolean isServer;

    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private final AtomicInteger nextPlayerId = new AtomicInteger(0);

    public GameEngine(double worldWidth, double worldHeight, boolean isServer) {
        this.entitiesMovable = new HashSet<>();
        this.entities = new HashSet<>();
        this.worldBounds = new WorldBounds(worldWidth, worldHeight);
        this.isServer = isServer;

        Boundary mapBoundary = new Boundary(0, 0, MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT);
        quadTree = new QuadTree(mapBoundary, QUAD_TREE_MAX_DEPTH);

    }


    public void update() {
        updateEntities();

        handleCollisions();

    }
    private void updateEntityInQuadTree(Entity entity) {
        // Supprimer l'entité de sa position actuelle dans le QuadTree
        quadTree.remove(entity);

        // Réinsérer l'entité à sa nouvelle position dans le QuadTree
        quadTree.insert(entity);
    }


    private void updateEntities() {
        for (Entity entity : entitiesMovable) {
            entity.Update();
            updateEntityInQuadTree(entity);
        }
    }




    private void handleCollisions() {
        for (Entity entity1 : entitiesMovable) {

            double detectionRange = entity1.sprite.getRadius()+  10;

            HashSet<Entity> nearbyEntities = getNearbyEntities(entity1, detectionRange);

            for (Entity entity2 : nearbyEntities) {
                if (checkCollision(entity1, entity2)) {
                    System.out.println("Collision detected between: " + entity1 + " and " + entity2);
                    handleCollision((MoveableBody) entity1, entity2);
                }
            }
        }
    }

    private boolean checkCollision(Entity entity1, Entity entity2) {
        Shape intersect = Shape.intersect(entity1.sprite, entity2.sprite);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            // Calculate intersection area
            double intersectionArea = intersect.getBoundsInLocal().getWidth() * intersect.getBoundsInLocal().getHeight();
            double entity1Area = Math.PI * Math.pow(entity1.sprite.getRadius(), 2);

            // Check if overlap is at least 33%
            return (intersectionArea / entity1Area) <= 0.33;
        }
        return false;
    }

    private void handleCollision(MoveableBody predator, Entity prey) {
        if (!entities.contains(prey)) {
            return;
        }


        if (checkCollision(predator, prey)) {
            if (canEat(predator, prey)) {
                removeEntity(prey);
                predator.increaseSize(prey.getMasse());
                prey.onDeletion();
            } else if (prey instanceof MoveableBody && canEat(prey, predator)) {
                removeEntity(predator);
                ((MoveableBody) prey).increaseSize(predator.getMasse());
                predator.onDeletion();
            }
        }
    }

    private boolean canEat(Entity predator, Entity prey) {
        double predatorArea = Math.PI * Math.pow(predator.sprite.getRadius(), 2);
        double preyArea = Math.PI * Math.pow(prey.sprite.getRadius(), 2);
        return predatorArea > preyArea * 1.33; // Must be 33% larger
    }



    public void addEntity(Entity entity) {
        entities.add(entity);
        quadTree.insert(entity);
        if(entity instanceof MoveableBody) {
            entitiesMovable.add(entity);
        }
    }


    public void removeEntity(Entity entity) {
        entities.remove(entity);
        entity.setPosition(-999999, -999999); //TODO Move entity off screen before removing

    }

    public int addPlayer(Player player) {
        int playerId = nextPlayerId.getAndIncrement();
        players.put(playerId, player);
        addEntity(player);
        return playerId;
    }

    public void removePlayer(int playerId) {
        Player player = players.remove(playerId);
        if (player != null) {
            removeEntity(player);
        }
    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public Map<Integer, Player> getPlayers() {
        return Collections.unmodifiableMap(players);
    }

    public HashSet<Entity> getEntities() {
        return entities;
    }

    public HashSet<Entity> getEntitiesOfType(Class<?> type) {
        return
            entities.stream()
                .filter(e -> type.isAssignableFrom(e.getClass()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public HashSet<Entity> getNearbyEntities(Entity entity, double range) {
        // Définir un boundary avec un rayon de recherche autour de l'entité
        Boundary searchBoundary = new Boundary(
                entity.sprite.getCenterX() - range,
                entity.sprite.getCenterY() - range,
                range * 2, // Largeur du carré = 2 * range
                range * 2  // Hauteur du carré = 2 * range
        );

        // Récupérer les entités dans la zone du QuadTree
        HashSet<Entity> nearbyEntities = new HashSet<>(quadTree.query(searchBoundary));

        // Exclure l'entité elle-même
        nearbyEntities.remove(entity);

        return nearbyEntities;
    }



}

record WorldBounds(double width, double height) {}