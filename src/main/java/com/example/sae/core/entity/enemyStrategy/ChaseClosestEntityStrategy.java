package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.controller.SoloController;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;

/**
 * AI strategy: seek out and attempt to devour the closest entity
 *
 * @see Enemy
 * @see RandomMoveStrategy
 * @see SeekFoodStrategy
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class ChaseClosestEntityStrategy implements EnemyStrategy {
    /**
     * Executes the AI's strategy of seeking out and attempting to devour the closest entity
     *
     * @see Enemy
     * @see RandomMoveStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param enemy Strategy executed on this AI
     * @return will return true if execution was successful
     */
    @Override
    public boolean execute(Enemy enemy) {
        GameEngine gameEngine = SoloController.getClient().getGameEngine();
        if (gameEngine == null) return false;

        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        // Find closest entity among existing entities
        for (Entity entity : gameEngine.getNearbyEntities(enemy, 400)) {
            if (entity != enemy && entity.getParent() != null && entity.getMasse()*1.33<enemy.getMasse() ) {
                double distance = enemy.distanceTo(entity.getPosition());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }

        // Move toward closest entity if found
        if (closestEntity != null) {
            enemy.moveToward(closestEntity.getPosition());
            return true;
        }
        return false;
    }
}