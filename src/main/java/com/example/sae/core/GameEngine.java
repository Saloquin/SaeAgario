package com.example.sae.core;

import com.example.sae.core.quadtree.Boundary;
import com.example.sae.core.quadtree.QuadTree;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.MoveableBody;
import com.example.sae.core.entity.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Shape;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameEngine {
    private final List<Entity> entities;
    private final List<Entity> entitiesToRemove;
    private final List<Entity> entitiesToAdd;
    private final WorldBounds worldBounds;

    private static final int QUAD_TREE_CAPACITY = 4;
    private static final int QUAD_TREE_MAX_DEPTH = 6;
    private static QuadTree quadTree;
    private static boolean gameStarted = false;

    public static final double MAP_LIMIT_WIDTH = 2000;
    public static final double MAP_LIMIT_HEIGHT = 2000;

    private final boolean isServer;

    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private final AtomicInteger nextPlayerId = new AtomicInteger(0);


    public GameEngine(double worldWidth, double worldHeight, boolean isServer) {
        this.entities = new CopyOnWriteArrayList<>();
        this.entitiesToRemove = new CopyOnWriteArrayList<>();
        this.entitiesToAdd = new CopyOnWriteArrayList<>();
        this.worldBounds = new WorldBounds(worldWidth, worldHeight);
        this.isServer = isServer;

        Boundary mapBoundary = new Boundary(0, 0, MAP_LIMIT_WIDTH/2, MAP_LIMIT_HEIGHT/2);
        quadTree = new QuadTree(mapBoundary, QUAD_TREE_CAPACITY, QUAD_TREE_MAX_DEPTH);
    }

    public void update() {

        // Reconstruire le QuadTree à chaque frame
        rebuildQuadTree();

        // Phase 1 : Mise à jour des entités
        updateEntities();

        // Phase 2 : Gestion des collisions
        handleCollisions();

        // Phase 3 : Gestion des mouvements et limites
        handleMovement();

        // Phase 4 : Nettoyage
        cleanupEntities();
    }

    private void rebuildQuadTree() {
        // Réinitialiser le QuadTree
        Boundary mapBoundary = new Boundary(0, 0, MAP_LIMIT_WIDTH/2, MAP_LIMIT_HEIGHT/2);
        quadTree = new QuadTree(mapBoundary, QUAD_TREE_CAPACITY, QUAD_TREE_MAX_DEPTH);

        // Insérer toutes les entités dans le QuadTree
        for (Entity entity : entities) {
            quadTree.insert(entity);
        }
    }

    private void handleCollisionsWithQuadTree() {
        for (Entity entity1 : entities) {
            if (entity1 instanceof MoveableBody moveable1) {
                // Créer une zone de recherche autour de l'entité
                double searchRadius = moveable1.Sprite.getRadius() * 2;
                Boundary searchArea = new Boundary(
                        moveable1.Sprite.getCenterX(),
                        moveable1.Sprite.getCenterY(),
                        searchRadius,
                        searchRadius
                );

                // Récupérer uniquement les entités proches via le QuadTree
                List<Entity> nearbyEntities = quadTree.query(searchArea);

                for (Entity entity2 : nearbyEntities) {
                    if (entity1 != entity2) {
                        if (checkCollision(moveable1, entity2)) {
                            handleCollision(moveable1, entity2);
                        }
                    }
                }
            }
        }
    }

private void updateEntities() {
    for (Entity entity : entities) {
        if (!isServer) {
            // En mode client
            if (entity instanceof Player) {
                // Le joueur local gère ses propres updates
                entity.Update();
            } else if (entity instanceof Enemy) {
                // Les ennemis doivent aussi être mis à jour en mode client
                entity.Update();
            }
        } else {
            // En mode serveur, on met à jour toutes les entités
            entity.Update();
        }
    }
}

    private void handleCollisions() {
        for (Entity entity1 : entities) {
            if (entity1 instanceof MoveableBody moveable1) {
                for (Entity entity2 : entities) {
                    if (entity1 != entity2) {
                        if (checkCollision(moveable1, entity2)) {
                            handleCollision(moveable1, entity2);
                        }
                    }
                }
            }
        }
    }


    private boolean checkCollision(Entity entity1, Entity entity2) {
        Shape intersect = Shape.intersect(entity1.Sprite, entity2.Sprite);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            // Calculate intersection area
            double intersectionArea = intersect.getBoundsInLocal().getWidth() * intersect.getBoundsInLocal().getHeight();
            double entity1Area = Math.PI * Math.pow(entity1.Sprite.getRadius(), 2);

            // Check if overlap is at least 33%
            return (intersectionArea / entity1Area) <= 0.33;
        }
        return false;
    }

    private void handleCollision(MoveableBody predator, Entity prey) {
        if (checkCollision(predator, prey) && canEat(predator, prey)) {
            removeEntity(prey);

            predator.increaseSize(prey.getMasse());
            prey.onDeletion();
        }
    }

    private boolean canEat(Entity predator, Entity prey) {
        double predatorArea = Math.PI * Math.pow(predator.Sprite.getRadius(), 2);
        double preyArea = Math.PI * Math.pow(prey.Sprite.getRadius(), 2);
        return predatorArea > preyArea * 1.33; // Must be 33% larger
    }


    private void handleMovement() {
        for (Entity entity : entities) {
            if (entity instanceof MoveableBody moveable) {
                moveWithinBounds(moveable);
            }
        }
    }


    private void moveWithinBounds(MoveableBody entity) {
        double[] currentPos = entity.getPosition();
        double[] newPos = new double[]{
                Math.max(-worldBounds.width(), Math.min(worldBounds.width(), currentPos[0])),
                Math.max(-worldBounds.height(), Math.min(worldBounds.height(), currentPos[1]))
        };
        entity.Sprite.setCenterX(newPos[0]);
        entity.Sprite.setCenterY(newPos[1]);
    }


    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
        entities.add(entity); // Ajout immédiat pour éviter les problèmes de synchronisation
        quadTree.insert(entity);
    }

    public void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
        entities.remove(entity); // Suppression immédiate pour éviter les problèmes de synchronisation
    }

    private void cleanupEntities() {
        entitiesToRemove.clear();
        entitiesToAdd.clear();
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

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntitiesOfType(Class<?> type) {
        return entities.stream()
                .filter(type::isInstance)
                .toList();
    }

    public List<Entity> getNearbyEntities(Entity entity, double range) {
        Boundary searchArea = new Boundary(
                entity.Sprite.getCenterX(),
                entity.Sprite.getCenterY(),
                range,
                range
        );
        return quadTree.query(searchArea);
    }

    public double getWorldWidth() {
        return worldBounds.width();
    }

    public double getWorldHeight() {
        return worldBounds.height();
    }



    public WorldBounds getWorldBounds() {
        return worldBounds;
    }
}

record WorldBounds(double width, double height) {}