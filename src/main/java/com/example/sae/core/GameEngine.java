package com.example.sae.core;

import com.example.sae.client.utils.config.Constants;
import com.example.sae.core.entity.*;
import com.example.sae.core.entity.powerUp.PowerUp;
import com.example.sae.core.quadtree.Boundary;
import com.example.sae.core.quadtree.QuadTree;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GameEngine {
    private final HashSet<Entity> entities;
    private final HashSet<Entity> entitiesToAdd;
    private final HashSet<Entity> entitiesToRemove;
    public final HashSet<MoveableBody> entitiesMovable;

    public final static double NB_FOOD_MAX = Constants.getNbFoodMax();
    public final static double NB_POWERUP_MAX = Constants.getNbPowerUpMax();
    public final static double NB_ENEMY_MAX = Constants.getNbEnemyMax();
    public final static double MASSE_INIT_PLAYER = Constants.getMasseInitPlayer();
    public final static double MASSE_INIT_FOOD = Constants.getMasseInitFood();
    public final static double MASSE_INIT_ENEMY = Constants.getMasseInitEnemy();
    public static final int ENEMY_RANGE = Constants.getEnemyRange();
    private static final int QUAD_TREE_MAX_DEPTH = Constants.getQuadTreeMaxDepth();
    public static final double MAP_LIMIT_WIDTH = Constants.getMapLimitWidth();
    public static final double MAP_LIMIT_HEIGHT = Constants.getMapLimitHeight();

    private static QuadTree quadTree;
    private boolean gameStarted = false;


    private final boolean isServer;

    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private final AtomicInteger nextPlayerId = new AtomicInteger(0);

    public GameEngine(double worldWidth, double worldHeight, boolean isServer) {
        this.entitiesMovable = new HashSet<>();
        this.entities = new HashSet<>();
        this.entitiesToAdd = new HashSet<>();
        this.entitiesToRemove = new HashSet<>();
        this.isServer = isServer;

        Boundary mapBoundary = new Boundary(0, 0, MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT);
        quadTree = new QuadTree(mapBoundary, QUAD_TREE_MAX_DEPTH);

    }

    public void update() {
        updateEntities();

        handleCollisions();

        cleanupEntities();
    }

    public List<MoveableBody> getEntitiesMovable() {
        return entitiesMovable.stream().toList();
    }

    private void updateEntityInQuadTree(Entity entity) {
        // Supprimer l'entité de sa position actuelle dans le QuadTree
        quadTree.remove(entity);

        // Réinsérer l'entité à sa nouvelle position dans le QuadTree
        quadTree.insert(entity);
    }

    private void updateEntities() {
        for (Entity entity : entitiesMovable) {
            updateEntityInQuadTree(entity);
            entity.Update();
        }
    }

    public void cleanupEntities() {
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }


    private void handleCollisions() {
        for (MoveableBody entity1 : entitiesMovable) {

            double detectionRange = entity1.getSprite().getRadius()+  10;

            HashSet<Entity> nearbyEntities = getNearbyEntities(entity1, detectionRange);
            System.out.println(entity1.getNom() +" "+nearbyEntities.toString());
            for (Entity entity2 : nearbyEntities) {
                if (checkCollision(entity1, entity2)) {
                    //DebugWindowController.addLog("Collision detected between: " + entity1 + " and " + entity2);
                    handleCollision(entity1, entity2);
                }
            }
        }
    }

    private boolean checkCollision(Entity entity1, Entity entity2) {
        Shape intersect = Shape.intersect(entity1.getSprite(), entity2.getSprite());
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            // Calculate intersection area
            double intersectionArea = intersect.getBoundsInLocal().getWidth() * intersect.getBoundsInLocal().getHeight();
            double entity1Area = Math.PI * Math.pow(entity1.getSprite().getRadius(), 2);

            return (intersectionArea / entity1Area) <= 0.33;
        }
        return false;
    }

    private void handleCollision(MoveableBody predator, Entity prey) {
        if (!entities.contains(prey)) {
            return;
        }

        if (checkCollision(predator, prey) && canEat(predator, prey)) {
            entities.remove(prey);
            TranslateTransition transition = new TranslateTransition(Duration.millis(200 ), prey.getSprite());

            double targetX = predator.getSprite().getCenterX() - prey.getSprite().getCenterX();
            double targetY = predator.getSprite().getCenterY() - prey.getSprite().getCenterY();

            transition.setToX(targetX);
            transition.setToY(targetY);

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(40), prey.getSprite());
            scaleTransition.setToX(0);
            scaleTransition.setToY(0);


            predator.increaseSize(prey.getMasse());
            if (prey instanceof PowerUp powerUp) {
                try{
                    powerUp.applyEffect(predator);
                }catch (Exception e)
                {
                    System.out.println("boule verte mangé");
                }
            }
            transition.setOnFinished(e -> {
                if (prey instanceof Player) {
                    int playerId = getPlayerId((Player) prey);
                    removePlayer(playerId);

                } else {
                    removeEntity(prey);
                }

                prey.onDeletion();
            });
            if(prey instanceof MoveableBody){
                ((MoveableBody) prey).deleteText();
            }
                transition.play();
                scaleTransition.play();

        }
    }

    private int getPlayerId(Player player) {
        return players.entrySet().stream()
                .filter(entry -> entry.getValue().equals(player))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    public boolean canEat(Entity predator, Entity prey) {
        return predator.getMasse() > prey.getMasse() * 1.33; // Must be 33% larger
    }




    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
        entities.add(entity);
        quadTree.insert(entity);
        if(entity instanceof MoveableBody) {
            entitiesMovable.add((MoveableBody)entity);
        }
    }


    public void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
        entities.remove(entity);
        entity.setPosition(9999999,9999999);

        if(entity instanceof MoveableBody) {
            entitiesMovable.remove((MoveableBody)entity);
            updateEntityInQuadTree(entity);
        }
        else{
            quadTree.remove(entity);
        }
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

    public HashSet<Entity> getEntitiesToAdd() {
        return entitiesToAdd;
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
                entity.getSprite().getCenterX() - range,
                entity.getSprite().getCenterY() - range,
                range * 2, // Largeur du carré = 2 * range
                range * 2  // Hauteur du carré = 2 * range
        );


        HashSet<Entity> nearbyEntities = new HashSet<>(quadTree.query(searchBoundary));

        // Exclure l'entité elle-même
        nearbyEntities.remove(entity);

        return nearbyEntities;
    }

    public List<MoveableBody> getSortedMovableEntities() {
        return entitiesMovable.stream()
                .map(entity -> (MoveableBody) entity)
                .sorted(Comparator.comparingDouble(MoveableBody::getMasse).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }



}
