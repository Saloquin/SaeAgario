package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.client.controller.SoloController;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.core.entity.enemyStrategy.EnemyStrategy;
import com.example.sae.core.entity.enemyStrategy.RandomMoveStrategy;
import com.example.sae.core.entity.enemyStrategy.SeekFoodStrategy;
import javafx.scene.Group;

import java.util.Random;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Enemy extends MoveableBody {
    private EnemyStrategy strategy;
    private static final Random random = new Random();
    private double[] targetPosition;
    private double strategyUpdateTimer = 0;
    private static final double STRATEGY_UPDATE_INTERVAL = 10.0; // Secondes

    public Enemy(Group group, double masse, String name) {
        super(group, masse, name);
        this.strategy = chooseOptimalStrategy();

        // Position initiale plus proche du centre
        double spreadFactor = 0.7; // Réduire la dispersion
        sprite.setCenterX((Math.random() * MAP_LIMIT_WIDTH * 2 -MAP_LIMIT_WIDTH) * spreadFactor);
        sprite.setCenterY((Math.random() * MAP_LIMIT_HEIGHT * 2 - MAP_LIMIT_HEIGHT) * spreadFactor);

        Speed = 1;
    }

    @Override
    public void Update() {
        strategy = chooseOptimalStrategy();
        if (strategy != null) {
            strategy.execute(this);
        }

    }

    public void setStrategy(EnemyStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean hasReachedTarget() {
        double distance = Math.sqrt(Math.pow(targetPosition[0] - sprite.getCenterX(), 2) + Math.pow(targetPosition[1] - sprite.getCenterY(), 2));
        return distance < 5;
    }

    public EnemyStrategy getStrategy() {
        return strategy;
    }

    public double[] getTargetPosition() {
        return targetPosition;
    }



    public void setTargetPosition(double[] targetPosition) {
        this.targetPosition = targetPosition;
    }

    private static EnemyStrategy getRandomStrategy() {
        int choice = random.nextInt(3); // 3 stratégies disponibles
        switch (choice) {
            case 0:
                return new RandomMoveStrategy();
            case 1:
                return new ChaseClosestEntityStrategy();
            case 2:
                return new SeekFoodStrategy();
            default:
                return new RandomMoveStrategy();
        }
    }

    private EnemyStrategy chooseOptimalStrategy() {
        GameEngine gameEngine = SoloController.getClient().getGameEngine();
        if (gameEngine == null) return new RandomMoveStrategy();

        var nearbyEntities = gameEngine.getNearbyEntities(this, 400);

        boolean hasValidPrey = nearbyEntities.stream()
                .anyMatch(entity -> entity.getMasse() <= this.getMasse() * 1.33
                        && entity.getParent() != null);

        boolean hasFoodNearby = nearbyEntities.stream()
                .anyMatch(entity -> entity instanceof Food);

        if (hasValidPrey) {
            return new ChaseClosestEntityStrategy();
        } else if (hasFoodNearby) {
            return new SeekFoodStrategy();
        } else {
            return new RandomMoveStrategy();
        }
    }

}
