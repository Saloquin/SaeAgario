package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.core.entity.Enemy;

import java.util.Random;
/**
 * AI strategy: move randomly
 *
 * @see Enemy
 * @see ChaseClosestEntityStrategy
 * @see SeekFoodStrategy
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class RandomMoveStrategy implements EnemyStrategy {
    /**
     * function to generate random numbers
     */
    private Random random = new Random();

    /**
     * number of seconds before updating AI direction
     */
    private static final double UPDATE_INTERVAL = 2.0; // Secondes

    /**
     * last time direction was updated in seconds
     */
    private double lastUpdateTime = 0;

    /**
     * Executes the AI's random move strategy
     *
     * @see Enemy
     * @see ChaseClosestEntityStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param enemy Strategy executed on this AI
     * @return will return true if execution was successful
     */
    @Override
    public boolean execute(Enemy enemy) {
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
        return true;
    }


}

