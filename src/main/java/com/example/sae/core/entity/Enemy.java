package com.example.sae.core.entity;

import com.example.sae.client.Client;
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
 * Entities controlled by enemy during local games
 *
 * @see MoveableBody
 * @see EnemyStrategy
 */
public class Enemy extends MoveableBody {
    /// random generator
    private static final Random random = new Random();
    /// Enemy current strategy
    private EnemyStrategy strategy;
    /// Enemy strategy's target position
    private double[] targetPosition;

    /**
     * @param group the group on which the enemy is created
     * @param mass the enemy's initial mass
     * @param name the enemy's name
     */
    public Enemy(Group group, double mass, String name) {
        super(group, mass, name);
        this.strategy = chooseOptimalStrategy();

        double spreadFactor = 0.7;
        // initial position as close as possible to the center
        sprite.setCenterX((Math.random() * MAP_LIMIT_WIDTH * 2 - MAP_LIMIT_WIDTH) * spreadFactor);
        sprite.setCenterY((Math.random() * MAP_LIMIT_HEIGHT * 2 - MAP_LIMIT_HEIGHT) * spreadFactor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void calculateSpeeds(double distanceFromCenter) {
        actualSpeedX = getMaxSpeed() * ENEMY_SPEED_MULTIPLIER;
        actualSpeedY = getMaxSpeed() * ENEMY_SPEED_MULTIPLIER;
    }

    /**
     * update the enemy's strategy
     *
     * @see EnemyStrategy
     */
    @Override
    public void Update() {
        strategy = chooseOptimalStrategy();
        strategy.execute(this);
    }

    /**
     * check if the enemy has reached its target
     *
     * @return true if the target is close enough, false otherwise
     */
    public boolean hasReachedTarget() {
        double distance = Math.sqrt(Math.pow(targetPosition[0] - sprite.getCenterX(), 2) + Math.pow(targetPosition[1] - sprite.getCenterY(), 2));
        return distance < 5;
    }

    /**
     * {@return the enemy's target position}
     */
    public double[] getTargetPosition() {
        return targetPosition;
    }

    /**
     * changes the enemy's target position
     *
     * @param targetPosition the new target's position
     */
    public void setTargetPosition(double[] targetPosition) {
        this.targetPosition = targetPosition;
    }

    /**
     * choose the current most optimal strategy for the enemy
     *
     * @return returns the chosen strategy
     * @see EnemyStrategy
     */
    private EnemyStrategy chooseOptimalStrategy() {
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return new RandomMoveStrategy();

        var nearbyEntities = gameEngine.getNearbyEntities(this, GameEngine.ENEMY_RANGE);

        boolean hasValidPrey = nearbyEntities.stream()
                .anyMatch(entity -> entity.getMasse() * 1.33 < this.getMasse()
                        && (entity instanceof Enemy || entity instanceof Player));

        boolean hasFoodNearby = nearbyEntities.stream()
                .anyMatch(entity -> entity.getMasse() * 1.33 < this.getMasse()
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
