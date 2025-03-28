package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.core.entity.Enemy;

import java.util.Random;

/**
 * Strategy making the enemy move randomly
 *
 * @see EnemyStrategy
 */
public class RandomMoveStrategy implements EnemyStrategy {
    ///  interval before switching strategy in seconds
    private static final double UPDATE_INTERVAL = 2.0;
    /// the random seed
    private final Random random = new Random();
    /// amount of time since the last update in seconds
    private double lastUpdateTime = 0;

    /**
     * {@inheritDoc}
     *
     * @param enemy {@inheritDoc}
     */
    @Override
    public void execute(Enemy enemy) {
        double currentTime = System.currentTimeMillis() / 1000.0;

        if (enemy.getTargetPosition() == null ||
                enemy.hasReachedTarget() ||
                currentTime - lastUpdateTime > UPDATE_INTERVAL) {

            // Largest moving distance possible
            double distance = random.nextDouble() * 800 + 200; // Entre 200 et 1000
            double angle = random.nextDouble() * 2 * Math.PI;

            // Calculate the new target position
            double[] currentPos = enemy.getPosition();
            double[] targetPosition = new double[]{
                    currentPos[0] + Math.cos(angle) * distance,
                    currentPos[1] + Math.sin(angle) * distance
            };

            enemy.setTargetPosition(targetPosition);
            lastUpdateTime = currentTime;
        }

        // Quicker movement
        if (enemy.getTargetPosition() != null) {
            enemy.moveToward(enemy.getTargetPosition());
        }
    }


}

