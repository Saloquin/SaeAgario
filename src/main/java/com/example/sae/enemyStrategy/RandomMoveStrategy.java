package com.example.sae.enemyStrategy;

import com.example.sae.entity.Enemy;

import java.util.Random;

public class RandomMoveStrategy implements EnemyStrategy {
    private Random random = new Random();
    private static final double UPDATE_INTERVAL = 2.0; // Secondes
    private double lastUpdateTime = 0;

    @Override
    public void execute(Enemy enemy) {
        double currentTime = System.currentTimeMillis() / 1000.0;

        if (enemy.getTargetPosition() == null ||
                enemy.hasReachedTarget() ||
                currentTime - lastUpdateTime > UPDATE_INTERVAL) {

            // Distance de déplacement plus grande
            double distance = random.nextDouble() * 800 + 200; // Entre 200 et 1000
            double angle = random.nextDouble() * 2 * Math.PI;

            // Calculer la nouvelle position cible
            double[] currentPos = enemy.getPosition();
            double[] targetPosition = new double[] {
                    currentPos[0] + Math.cos(angle) * distance,
                    currentPos[1] + Math.sin(angle) * distance
            };

            enemy.setTargetPosition(targetPosition);
            lastUpdateTime = currentTime;
        }

        // Déplacement plus rapide
        if (enemy.getTargetPosition() != null) {
            enemy.moveToward(enemy.getTargetPosition());
        }
    }


}

