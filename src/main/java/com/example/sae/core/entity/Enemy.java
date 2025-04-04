package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.client.Client;
import com.example.sae.client.controller.SoloController;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.core.entity.enemyStrategy.EnemyStrategy;
import com.example.sae.core.entity.enemyStrategy.RandomMoveStrategy;
import com.example.sae.core.entity.enemyStrategy.SeekFoodStrategy;
import com.example.sae.core.entity.powerUp.PowerUp;
import javafx.scene.Group;

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

    @Override
    protected void calculateSpeeds(double distanceFromCenter) {
        actualSpeedX = getMaxSpeed() * ENEMY_SPEED_MULTIPLIER;
        actualSpeedY = getMaxSpeed() * ENEMY_SPEED_MULTIPLIER;
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
        strategy = chooseOptimalStrategy();
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
        if (targetPosition == null) {
            return false;
        }
        double distance = Math.sqrt(Math.pow(targetPosition[0] - sprite.getCenterX(), 2) + Math.pow(targetPosition[1] - sprite.getCenterY(), 2));
        return distance < sprite.getRadius() +5;
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

}
