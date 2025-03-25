package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.entity.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.core.entity.enemyStrategy.EnemyStrategy;
import com.example.sae.core.entity.enemyStrategy.RandomMoveStrategy;
import com.example.sae.core.entity.enemyStrategy.SeekFoodStrategy;
import javafx.scene.Group;

import java.util.Random;

public class Enemy extends MoveableBody {
    private EnemyStrategy strategy;
    private static final Random random = new Random();
    private double[] targetPosition;
    private double strategyUpdateTimer = 0;
    private static final double STRATEGY_UPDATE_INTERVAL = 10.0; // Secondes

    public Enemy(Group group, double masse) {
        super(group, masse);
        this.strategy = getRandomStrategy();

        // Position initiale plus proche du centre
        double spreadFactor = 0.7; // Réduire la dispersion
        Sprite.setCenterX((Math.random() * AgarioApplication.getMapLimitWidth() * 2 - AgarioApplication.getMapLimitWidth()) * spreadFactor);
        Sprite.setCenterY((Math.random() * AgarioApplication.getMapLimitHeight() * 2 - AgarioApplication.getMapLimitHeight()) * spreadFactor);

        Speed = 2.0; // Vitesse de base plus élevée
    }

    @Override
    public void Update() {
        // Changer de stratégie périodiquement
        strategyUpdateTimer += 0.016; // Approximativement 60 FPS
        if (strategyUpdateTimer >= STRATEGY_UPDATE_INTERVAL) {
            strategy = getRandomStrategy();
            strategyUpdateTimer = 0;
        }

        if (strategy != null) {
            strategy.execute(this);
        }

        checkCollision();
    }

    public void setStrategy(EnemyStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean hasReachedTarget() {
        double distance = Math.sqrt(Math.pow(targetPosition[0] - Sprite.getCenterX(), 2) + Math.pow(targetPosition[1] - Sprite.getCenterY(), 2));
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


    @Override
    public void onDeletion() {
        // Retirer du graphe de scène JavaFX si nécessaire
        if (getParent() != null) {
            ((Group) getParent()).getChildren().remove(this);
        }
        // Pas besoin de décrémenter enemies-- car cela devrait être géré par GameEngine
        // qui maintient sa propre liste d'entités
    }
}
