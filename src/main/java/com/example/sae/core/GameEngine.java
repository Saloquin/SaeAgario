package com.example.sae.core;

import com.example.sae.client.Client;
import com.example.sae.client.controller.SoloController;
import com.example.sae.client.controller.managers.MinimapManager;
import com.example.sae.client.controller.managers.PlayerInfoManager;
import com.example.sae.client.utils.config.Constants;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.movable.body.BodyComposite;
import com.example.sae.core.entity.movable.body.MoveableBody;
import com.example.sae.core.entity.movable.Player;
import com.example.sae.core.entity.immobile.powerUp.PowerUp;
import com.example.sae.core.quadtree.Boundary;
import com.example.sae.core.quadtree.QuadTree;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Managed all the game functionality and contains elements of the game
 */
public class GameEngine {
    /// Contains all the entities of the game.
    private final HashSet<Entity> entities;
    /// Contains all entities to add  on the next update.
    private final HashSet<Entity> entitiesToAdd;
    /// Contains all entities to remove on the next update.
    private final HashSet<Entity> entitiesToRemove;
    /// Contains all entities Movable of the game.
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



    /// True if this is a server.
    private final boolean isServer;
    /// Contains all players with a unique id
    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    /// Generate unique Id for players
    private final AtomicInteger nextPlayerId = new AtomicInteger(0);
    private final boolean gameStarted = false;
    private static Pane root;

    /**
     * Constructor of the class
     * @param isServer True when it's a server
     */
    public GameEngine(boolean isServer) {
        clearRoot();
        this.entitiesMovable = new HashSet<>();
        this.entities = new HashSet<>();
        this.entitiesToAdd = new HashSet<>();
        this.entitiesToRemove = new HashSet<>();
        this.isServer = isServer;

        Boundary mapBoundary = new Boundary(0, 0, MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT);
        quadTree = new QuadTree(mapBoundary, QUAD_TREE_MAX_DEPTH);

    }

    public static void clearRoot() {
        root = new Pane();
    }

    public static Pane getRoot() {
        return root;
    }

    /**
     * Contains all functions needed to update the game
     */
    public void update() {
        updateEntities();

        handleCollisions();

        cleanupEntities();
    }

    /**
     * Get all entitiesMovable
     * @return A {@link List} of entitiesMovable
     */
    public List<MoveableBody> getEntitiesMovable() {
        return entitiesMovable.stream().toList();
    }

    /**
     * Update the entity passed in parameter in the quad tree
     * @param entity The entity to update
     */
    private void updateEntityInQuadTree(Entity entity) {
        // Delete the entity from its current position in the QuadTree
        quadTree.remove(entity);

        // Insert the entity to its new position in the Quad Tree
        quadTree.insert(entity);
    }

    /**
     * Update all Movable entities
     */
    private void updateEntities() {
        for (Entity entity : entitiesMovable) {
            updateEntityInQuadTree(entity);
            entity.Update();
        }
        for (Player p : players.values()) {
            if(p.getSprite().getCenterY()==9999999 && p.getSprite().getCenterX()==9999999){
                removePlayer(getPlayerId(p));
            }
        }
    }

    /**
     * Clean all entities before the next update
     */
    public void cleanupEntities() {
        entitiesToAdd.clear();
        entitiesToRemove.clear();
    }

    /**
     * Handle collisions between entities
     */
    private void handleCollisions() {
        // Créer une copie de la collection pour l'itération
        List<MoveableBody> entities = new ArrayList<>(entitiesMovable);

        for (MoveableBody entity1 : entities) {
            double detectionRange = entity1.getSprite().getRadius() + 10;
            HashSet<Entity> nearbyEntities = getNearbyEntities(entity1, detectionRange);

            for (Entity entity2 : nearbyEntities) {
                if (checkCollision(entity1, entity2)) {
                    handleCollision(entity1, entity2);
                }
            }
        }
    }

    /**
     * Check if a collision occurred between two entities
     * @param entity1 An entity to check
     * @param entity2 An entity to check
     * @return {@code true} if a collision is detected {@code false} otherwise
     */
    private boolean checkCollision(Entity entity1, Entity entity2) {
        Shape intersect = Shape.intersect(entity1.getSprite(), entity2.getSprite());
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            // Calculer l'aire de l'intersection
            double intersectionArea = intersect.getBoundsInLocal().getWidth() * intersect.getBoundsInLocal().getHeight();
            double entity2Area = Math.PI * Math.pow(entity2.getSprite().getRadius(), 2);
            double entity1Area = Math.PI * Math.pow(entity1.getSprite().getRadius(), 2);

            // L'entité 1 doit recouvrir au moins 33% de l'entité 2
            double coverageRatio = intersectionArea / entity2Area;
            return coverageRatio >= 0.33;
        }
        return false;
    }

    /**
     * Managed the collision between two entities
     * @param predator the entity that may be eating
     * @param prey the entity maybe eaten
     */
    private void handleCollision(MoveableBody predator, Entity prey) {
        if (!entities.contains(prey) || !checkCollision(predator, prey) || !canEat(predator, prey)) {
            return;
        }
        removeFromCollections(prey);
        predator.increaseSize(prey.getMasse());
        startAnimations(predator, prey);
        System.out.println(isCloneCollision(predator, prey));
        if (isMainBodyCollision(predator, prey)) {
            handleMainBodyCollision(predator, (MoveableBody) prey);
        } else if (isCloneCollision(predator, prey)) {
            handleCloneCollision(predator, (MoveableBody) prey);
        } else if (isMainBodyCollisonWithOther(predator, prey)) {
            handleMainBodyCollisionWithOther(predator, (MoveableBody) prey);
        } else {
            applyCollisionEffects(predator, prey);
        }
    }

    private void handleMainBodyCollisionWithOther(MoveableBody predator, MoveableBody prey) {
        if(prey.getComposite().getClones().isEmpty()) {
            if(prey instanceof Player player) {
                removePlayer(getPlayerId(player));
            }
            removeEntity(prey);
            entitiesMovable.remove(prey);
            quadTree.remove(prey);
        }
        else {
            MoveableBody firstClone = (MoveableBody) prey.getComposite().getClones().get(0);
            BodyComposite composite = firstClone.getComposite();
            composite.setMainBody(firstClone);
            composite.removeClone(firstClone);

            removeFromCollections(prey);
            removeEntity(prey);
            prey.onDeletion();

            if (prey instanceof Player oldPlayer && firstClone instanceof Player newPlayer) {
                handlePlayerMainBodyCollision(oldPlayer, newPlayer);
            }
        }
    }

    private boolean isMainBodyCollisonWithOther(MoveableBody predator, Entity prey) {
        return prey instanceof MoveableBody preyBody &&
                !preyBody.belongsToSameComposite(predator);
    }

    private void removeFromCollections(Entity entity) {
        entities.remove(entity);
        entitiesMovable.remove(entity);
        quadTree.remove(entity);
    }

    private void startAnimations(MoveableBody predator, Entity prey) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(200), prey.getSprite());
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(40), prey.getSprite());

        // Animation translation
        double targetX = predator.getSprite().getCenterX() - prey.getSprite().getCenterX();
        double targetY = predator.getSprite().getCenterY() - prey.getSprite().getCenterY();
        transition.setToX(targetX);
        transition.setToY(targetY);

        // Animation réduction
        scaleTransition.setToX(0);
        scaleTransition.setToY(0);

        if (prey instanceof MoveableBody) {
            ((MoveableBody) prey).deleteText();
        }

        transition.setOnFinished(e -> prey.onDeletion());
        transition.play();
        scaleTransition.play();
    }

    private boolean isMainBodyCollision(MoveableBody predator, Entity prey) {
        return prey instanceof MoveableBody preyBody &&
                predator.belongsToSameComposite(preyBody) &&
                preyBody == predator.getComposite().getMainBody();
    }

    private boolean isCloneCollision(MoveableBody predator, Entity prey) {
        return prey instanceof MoveableBody preyBody && predator.belongsToSameComposite(preyBody);
    }

    private void handleMainBodyCollision(MoveableBody predator, MoveableBody prey) {
        BodyComposite composite = predator.getComposite();
        
        composite.setMainBody(predator);
        composite.removeClone(predator);

        removeFromCollections(prey);
        removeEntity(prey);
        prey.onDeletion();


        if (prey instanceof Player oldPlayer && predator instanceof Player newPlayer) {
            handlePlayerMainBodyCollision(oldPlayer, newPlayer);
        }
    }

    private void handlePlayerMainBodyCollision(Player oldPlayer, Player newPlayer) {
        int oldPlayerId = getPlayerId(oldPlayer);
        if (oldPlayerId != -1) {
            removePlayer(oldPlayerId);
            removeEntity(oldPlayer);
            entitiesMovable.remove(oldPlayer);
            quadTree.remove(oldPlayer);
            players.put(oldPlayerId, newPlayer);
            Camera.focusPaneOn(SoloController.getPane(), newPlayer);
        }
    }



    private void handleCloneCollision(MoveableBody predator, MoveableBody prey) {
        prey.getComposite().removeClone(prey);
        predator.getComposite().removeClone(prey);
        removeFromCollections(prey);
        removeEntity(prey);
        prey.onDeletion();
    }

    private void applyCollisionEffects(MoveableBody predator, Entity prey) {
        if (prey instanceof PowerUp powerUp) {
            try {
                powerUp.applyEffect(predator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private int getPlayerId(Player player) {
        return players.entrySet().stream()
                .filter(entry -> entry.getValue().equals(player))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    /**
     * Check if the predator can eat the prey
     * @param predator the entity that may be eating
     * @param prey the entity maybe eaten
     * @return {@code true} if the predator can eat the prey {@code false} otherwise
     */
    public boolean canEat(Entity predator, Entity prey) {
        // Vérification des composites
        if (predator instanceof MoveableBody predatorBody &&
                prey instanceof MoveableBody preyBody) {
            // Si même composite
            if (predatorBody.belongsToSameComposite(preyBody)) {
                return predatorBody.getComposite().canClonesEatEachOther() &&
                        predator.getMasse() >= prey.getMasse();
            }
        }

        return predator.getMasse() > prey.getMasse() * 1.33;
    }


    /**
     * add an entity to the game
     * @param entity the entity to add
     */
    public void addEntity(Entity entity) {
        entitiesToAdd.add(entity);
        entities.add(entity);
        quadTree.insert(entity);
        if (entity instanceof MoveableBody) {
            entitiesMovable.add((MoveableBody) entity);
        }
    }

    /**
     * remove an entity of the game
     * @param entity the entity to remove
     */
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

    /**
     * Add a player to the game
     * @param player The player to add
     * @return The player's id added
     */
    public int addPlayer(Player player) {
        int playerId = nextPlayerId.getAndIncrement();
        players.put(playerId, player);
        addEntity(player);
        return playerId;
    }

    /**
     * Remove a player of the game
     * @param playerId The playerId to remove
     */
    public void removePlayer(int playerId) {
        Player player = players.remove(playerId);
        if (player != null) {
            removeEntity(player);
        }

    }

    /**
     * Get the Player of the playerId passed in parameter
     * @param playerId The playerId to get
     * @return The player
     */
    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    /**
     * Get all entities of the game
     * @return an {@link HashSet} with all entities
     */
    public HashSet<Entity> getEntities() {
        return entities;
    }

    /**
     * Get all entities of the type passed in parameter
     * @param type The type of entities
     * @return An {@link HashSet} of all type entities
     */
    public HashSet<Entity> getEntitiesOfType(Class<?> type) {
        return
                entities.stream()
                        .filter(e -> type.isAssignableFrom(e.getClass()))
                        .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Get nearby entities of the entity passed in parameter
     * @param entity The entity
     * @param range The range of checking
     * @return An {@link HashSet} of nearby entities
     */
    public HashSet<Entity> getNearbyEntities(Entity entity, double range) {
        // Define a boundary with a search radius around the entity
        Boundary searchBoundary = new Boundary(
                entity.getSprite().getCenterX() - range,
                entity.getSprite().getCenterY() - range,
                range * 2, // Width du carré = 2 * range
                range * 2  // Height of square = 2 * range
        );


        HashSet<Entity> nearbyEntities = new HashSet<>(quadTree.query(searchBoundary));

        // Exclude the entity itself
        nearbyEntities.remove(entity);

        return nearbyEntities;
    }

    /**
     * Get a sorted List of movable entities of the game
     * @return a {@link List} of movable body
     */
    public List<MoveableBody> getSortedMovableEntities() {
        return entitiesMovable.stream()
                .map(entity -> (MoveableBody) entity)
                .sorted(Comparator.comparingDouble(MoveableBody::getTotalMasse).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }


}
