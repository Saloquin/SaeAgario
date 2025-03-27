package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.Client;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Player;

import java.util.HashSet;

/**
 * AI strategy: seek out and attempt to devour the closest entity
 *
 * @see Enemy
 * @see RandomMoveStrategy
 * @see SeekFoodStrategy
 */
public class ChaseClosestEntityStrategy implements EnemyStrategy {
    /**
     * Executes the AI's strategy of seeking out and attempting to devour the closest entity
     *
     * @param enemy Strategy executed on this AI
     * @return will return true if execution was successful
     * @see Enemy
     * @see RandomMoveStrategy
     * @see SeekFoodStrategy
     */
    @Override
    public boolean execute(Enemy enemy) {
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return false;

        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        HashSet<Entity> nearbyEntities = gameEngine.getNearbyEntities(enemy, GameEngine.ENEMY_RANGE);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Enemy || entity instanceof Player) {
                if (enemy.getMasse() > entity.getMasse() * 1.33) {
                    double distance = enemy.distanceTo(entity.getPosition());
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestEntity = entity;
                    }
                }
            }
        }

        if (closestEntity != null) {
            enemy.moveToward(closestEntity.getPosition());
            return true;
        } else {
            new SeekFoodStrategy().execute(enemy);
            return false;
        }
    }
}