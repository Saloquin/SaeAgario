package com.example.sae.core.entity.movable;

import com.example.sae.client.Client;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.immobile.Food;
import com.example.sae.core.entity.movable.body.BodyComponent;
import com.example.sae.core.entity.movable.body.MoveableBody;
import com.example.sae.core.entity.movable.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.core.entity.movable.enemyStrategy.EnemyStrategy;
import com.example.sae.core.entity.movable.enemyStrategy.RandomMoveStrategy;
import com.example.sae.core.entity.movable.enemyStrategy.SeekFoodStrategy;
import com.example.sae.core.entity.immobile.powerUp.PowerUp;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Random;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

/**
 * AI on local gaming
 *
 * @see MoveableBody
 * @see EnemyStrategy
 * @see RandomMoveStrategy
 * @see ChaseClosestEntityStrategy
 * @see SeekFoodStrategy
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class Enemy extends MoveableBody {
    /**
     * AI strategy
     */
    private EnemyStrategy strategy;

    /**
     * function to generate random numbers
     */
    private static final Random random = new Random();

    private double[] targetPosition;

    /**
     * last time strategy was updated in seconds
     */
    private double strategyUpdateTimer = 0;

    /**
     * number of seconds before updating AI strategy
     */
    private static final double STRATEGY_UPDATE_INTERVAL = 10.0; // Secondes

    /**
     * constructor
     *
     * @see MoveableBody
     * @see EnemyStrategy
     * @see RandomMoveStrategy
     * @see ChaseClosestEntityStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param masse masse of IA
     * @param name name of IA
     */
    public Enemy(Group group, double masse, String name) {
        super(group, masse, name);
        this.strategy =chooseOptimalStrategy();
        // Position initiale plus proche du centre
        double spreadFactor = 0.7; // Réduire la dispersion
        sprite.setCenterX((Math.random() * MAP_LIMIT_WIDTH * 2 -MAP_LIMIT_WIDTH) * spreadFactor);
        sprite.setCenterY((Math.random() * MAP_LIMIT_HEIGHT * 2 - MAP_LIMIT_HEIGHT) * spreadFactor);
    }
    public Enemy(Group group, double masse, String name, Color color) {
        super(group, masse, color, name);
        this.strategy =((Enemy)composite.getMainBody()).getStrategy();
    }

    @Override
    protected void calculateSpeeds(double distanceFromCenter) {
        actualSpeedX = getMaxSpeed() * ENEMY_SPEED_MULTIPLIER;
        actualSpeedY = getMaxSpeed() * ENEMY_SPEED_MULTIPLIER;
    }
    @Override
    public void splitSprite() {
        Enemy clone = EntityFactory.createEnemy(getMasse()/2,getNom(), (Color) sprite.getFill());
        clone.sprite.setCenterX(sprite.getCenterX() + CLONE_SPLIT_DISTANCE);
        clone.sprite.setCenterY(sprite.getCenterY() + CLONE_SPLIT_DISTANCE);
        setMasse(getMasse() / 2);
        clone.setComposite(this.composite);
        Client.getGameEngine().addEntity(clone);
        addClone(clone);
        this.composite.updateLastSplitTime();
    }

    /**
     * update IA strategy
     *
     * @see MoveableBody
     * @see EnemyStrategy
     * @see RandomMoveStrategy
     * @see ChaseClosestEntityStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    @Override
    public void Update() {
        if (/*composite != null && */composite.getMainBody() != this) {
            // Si c'est un clone, copie la cible du corps principal
            Enemy mainBody = (Enemy) composite.getMainBody();
            this.targetPosition = mainBody.getTargetPosition();
            // Garde sa propre stratégie mais met à jour
            strategy = chooseOptimalStrategy();
        } else {
            // Si c'est le corps principal, choisit une nouvelle stratégie
            strategy = chooseOptimalStrategy();
        }

        // Exécute la stratégie
        System.out.println(Arrays.toString(getTargetPosition()));
        strategy.execute(this);
    }

    /**
     * change IA strategy
     *
     * @see EnemyStrategy
     * @see RandomMoveStrategy
     * @see ChaseClosestEntityStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param strategy new strategy
     */
    public void setStrategy(EnemyStrategy strategy) {
        this.strategy = strategy;
    }


    /**
     * indicates whether the AI has reached the target
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns true if the distance between the target and the AI are small enough to be reached
     */
    public boolean hasReachedTarget() {
        double distance = Math.sqrt(Math.pow(targetPosition[0] - sprite.getCenterX(), 2) + Math.pow(targetPosition[1] - sprite.getCenterY(), 2));
        return distance < 5;
    }

    /**
     * returns to AI strategy
     *
     * @see EnemyStrategy
     * @see RandomMoveStrategy
     * @see ChaseClosestEntityStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns to AI strategy
     */
    public EnemyStrategy getStrategy() {
        return strategy;
    }

    /**
     * returns target position
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns target position
     */
    public double[] getTargetPosition() {
        return targetPosition;
    }

    /**
     * changes the target
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param targetPosition new position of the target
     */
    public void setTargetPosition(double[] targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * returns a random strategy for the AI
     *
     * @see EnemyStrategy
     * @see RandomMoveStrategy
     * @see ChaseClosestEntityStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns a random strategy for the AI
     */
    private static EnemyStrategy getRandomStrategy() {
        int choice = random.nextInt(3); // 3 stratégies disponibles
        return switch (choice) {
            case 1 -> new ChaseClosestEntityStrategy();
            case 2 -> new SeekFoodStrategy();
            default -> new RandomMoveStrategy();
        };
    }

    /**
     * chosen and gives the most optimal strategy for the AI
     *
     * @see EnemyStrategy
     * @see RandomMoveStrategy
     * @see ChaseClosestEntityStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns the most optimal strategy based on the situation
     */
    private EnemyStrategy chooseOptimalStrategy() {
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return new RandomMoveStrategy();

        var nearbyEntities = gameEngine.getNearbyEntities(this, GameEngine.ENEMY_RANGE);

        boolean hasValidPrey = nearbyEntities.stream()
                .anyMatch(entity -> entity.getMasse()*1.33 < this.getMasse()
                        && (entity instanceof Enemy || entity instanceof Player));

        boolean hasFoodNearby = nearbyEntities.stream()
                .anyMatch(entity -> entity.getMasse()*1.33 < this.getMasse()
                        && (entity instanceof Food || entity instanceof PowerUp));
        if (hasValidPrey) {
            return new ChaseClosestEntityStrategy();
        } else if (hasFoodNearby) {
            return new SeekFoodStrategy();
        } else {
            return new RandomMoveStrategy();
        }
    }

    public void updateTargetForClones(double[] newTarget) {
        setTargetPosition(newTarget);
        if (composite != null && composite.getMainBody() == this) {
            for (BodyComponent clone : composite.getClones()) {
                if (clone instanceof Enemy enemy) {
                    enemy.setTargetPosition(newTarget);
                }
            }
        }
    }

}
