package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.core.entity.Enemy;

import java.util.Random;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class RandomMoveStrategy implements EnemyStrategy {
    private Random random = new Random();
    private static final double UPDATE_INTERVAL = 3.0;
    private static final double MAX_TURN_ANGLE = Math.PI / 4; // 45 degrees
    private long lastUpdateTime = System.nanoTime();

    @Override
    public boolean execute(Enemy enemy) {
        long currentTime = System.nanoTime();
        double elapsedTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;

        if (enemy.getTargetPosition() == null || enemy.hasReachedTarget() || elapsedTime > UPDATE_INTERVAL) {
            double[] currentPosition = enemy.getPosition();
            double[] targetPosition = generateNewTargetPosition(currentPosition, enemy.getTargetPosition());

            enemy.setTargetPosition(targetPosition);
            lastUpdateTime = currentTime;
        }

        if (enemy.getTargetPosition() != null) {
            enemy.moveToward(enemy.getTargetPosition());
        }
        return true;
    }

    private double[] generateNewTargetPosition(double[] currentPosition, double[] previousTargetPosition) {
        double angle;
        if (previousTargetPosition == null) {
            angle = random.nextDouble() * 2 * Math.PI;
        } else {
            double currentAngle = Math.atan2(previousTargetPosition[1] - currentPosition[1], previousTargetPosition[0] - currentPosition[0]);
            angle = currentAngle + (random.nextDouble() * 2 * MAX_TURN_ANGLE - MAX_TURN_ANGLE);
        }

        double distance = random.nextDouble() * Math.min(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT) / 2;
        double newX = currentPosition[0] + Math.cos(angle) * distance;
        double newY = currentPosition[1] + Math.sin(angle) * distance;

        // Ensure the new position is within the map limits
        newX = Math.max(-MAP_LIMIT_WIDTH, Math.min(MAP_LIMIT_WIDTH, newX));
        newY = Math.max(-MAP_LIMIT_HEIGHT, Math.min(MAP_LIMIT_HEIGHT, newY));

        return new double[]{newX, newY};
    }
}