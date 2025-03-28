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
    public Enemy(Group group, double masse, String name, Color color) {
        super(group, masse, color, name);
        this.strategy =((Enemy)composite.getMainBody()).getStrategy();
    }

    /**
     * {@inheritDoc}
     */
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
     * update the enemy's strategy
     *
     * @see EnemyStrategy
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
     * check if the enemy has reached its target
     *
     * @return true if the target is close enough, false otherwise
     */
    public boolean hasReachedTarget() {
        double distance = Math.sqrt(Math.pow(targetPosition[0] - sprite.getCenterX(), 2) + Math.pow(targetPosition[1] - sprite.getCenterY(), 2));
        return distance < 5;
    }

    /**
     * {@return the enemy strategy}
     * @see EnemyStrategy
     */
    public EnemyStrategy getStrategy() {
        return strategy;
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
