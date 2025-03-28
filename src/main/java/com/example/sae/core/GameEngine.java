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

    private boolean checkCollision(Entity entity1, Entity entity2) {
        Shape intersect = Shape.intersect(entity1.getSprite(), entity2.getSprite());
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            // Calculer l'aire de l'intersection
            double intersectionArea = intersect.getBoundsInLocal().getWidth() * intersect.getBoundsInLocal().getHeight();
            double entity2Area = Math.PI * Math.pow(entity2.getSprite().getRadius(), 2);

            // Si l'aire d'intersection est supérieure à 66% de l'aire de la proie
            // OU si l'intersection est inférieure à 33% de l'aire du prédateur
            return (intersectionArea / entity2Area) >= 0.66 ||
                    (intersectionArea / (Math.PI * Math.pow(entity1.getSprite().getRadius(), 2))) <= 0.33;
        }
        return false;
    }

    private void handleCollision(MoveableBody predator, Entity prey) {
        if (!entities.contains(prey) || !checkCollision(predator, prey) || !canEat(predator, prey)) {
            return;
        }
        removeFromCollections(prey);
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
        if(prey.getComposite().getClones().isEmpty()){
            System.out.println("Plus de clones, game over");
            if(prey instanceof Player player) {
                removePlayer(getPlayerId(player));
            }
            removeEntity(prey);
            entitiesMovable.remove(prey);
            quadTree.remove(prey);
        }
        else{
            System.out.println("manger par predateur autre que moi tandis que j'ai un composite");


            MoveableBody firstClone = (MoveableBody) prey.getComposite().getClones().get(0);
            BodyComposite composite = firstClone.getComposite();
            composite.setMainBody(firstClone);
            composite.removeClone(firstClone);

            if (prey instanceof Player oldPlayer && firstClone instanceof Player newPlayer) {
                handlePlayerMainBodyCollision(oldPlayer, newPlayer);
            }
//            // Update camera focus if predator is a Player
//            if (firstClone instanceof Player player) {
//                Client.getCamera().focusPaneOn(MinimapManager.getInstance().getPlayerView(), player);
//                MinimapManager.getInstance().setPlayer(player);
//                PlayerInfoManager.getInstance().setPlayer(player);
//            }
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

        // Supprimer le texte avant l'animation si c'est un MoveableBody
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

        if (prey instanceof Player oldPlayer && predator instanceof Player newPlayer) {
            handlePlayerMainBodyCollision(oldPlayer, newPlayer);
        }

        BodyComposite composite = predator.getComposite();
        composite.setMainBody(predator);
        composite.removeClone(predator);

        System.out.println("manger par mon clone");
    }

    private void handlePlayerMainBodyCollision(Player oldPlayer, Player newPlayer) {
        int oldPlayerId = getPlayerId(oldPlayer);
        if (oldPlayerId != -1) {
            removePlayer(oldPlayerId);
            removeEntity(oldPlayer);
            entitiesMovable.remove(oldPlayer);
            quadTree.remove(oldPlayer);
            players.put(oldPlayerId, newPlayer);
            Client.getCamera().focusPaneOn(SoloController.getPane(), newPlayer);
//            MinimapManager.getInstance().setPlayer(newPlayer);
//            PlayerInfoManager.getInstance().setPlayer(newPlayer);   //TODO() reparer les affichage
        }
    }



    private void handleCloneCollision(MoveableBody predator, MoveableBody prey) {
        System.out.println("je mange mon clone");
        prey.getComposite().removeClone(prey);
    }

    private void applyCollisionEffects(MoveableBody predator, Entity prey) {
        predator.increaseSize(prey.getMasse());
        if (prey instanceof PowerUp powerUp) {
            try {
                powerUp.applyEffect(predator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("manger par predateur autre que moi tandis que je n'ai pas de composite");
        /*if(prey instanceof Player player){
            removePlayer(getPlayerId(player));
        }*/
    }

    private int getPlayerId(Player player) {
        return players.entrySet().stream()
                .filter(entry -> entry.getValue().equals(player))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    public boolean canEat(Entity predator, Entity prey) {
        // Vérification des composites
        if (predator instanceof MoveableBody predatorBody &&
                prey instanceof MoveableBody preyBody) {
            // Si même composite
            if (predatorBody.belongsToSameComposite(preyBody)) {
                // Les clones peuvent se manger si le cooldown est passé
                // et si le prédateur est plus grand ou égal à la proie
                return predatorBody.getComposite().canClonesEatEachOther() &&
                        predator.getMasse() >= prey.getMasse();
            }
        }

        // Vérification normale de la taille (pour les entités de différents composites)
        return predator.getMasse() > prey.getMasse() * 1.33;
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
                .sorted(Comparator.comparingDouble(MoveableBody::getTotalMasse).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }


}
