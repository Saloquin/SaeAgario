package com.example.sae;

import com.example.sae.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.enemyStrategy.EnemyStrategy;
import com.example.sae.enemyStrategy.RandomMoveStrategy;
import com.example.sae.enemyStrategy.SeekFoodStrategy;
import javafx.scene.Group;

import java.util.Random;

public class Enemy extends MoveableBody {
    private EnemyStrategy strategy;
    private static final Random random = new Random();
    private double[] targetPosition;
    public Enemy(Group group, double masse) {
        super(group, masse);
        this.strategy = getRandomStrategy();

        Sprite.setCenterX((Math.random() * AgarioApplication.getMapLimitWidth() * 2) - AgarioApplication.getMapLimitWidth());
        Sprite.setCenterY((Math.random() * AgarioApplication.getMapLimitHeight() * 2) - AgarioApplication.getMapLimitHeight());
    }

    @Override
    public void Update() {
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
        AgarioApplication.enemies--;
    }
}
