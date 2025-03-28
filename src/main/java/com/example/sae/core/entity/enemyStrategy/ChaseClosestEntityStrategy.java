package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.Client;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Player;

import java.util.HashSet;

/**
 * Strategy making the enemy chase the closest moving entity
 *
 * @see EnemyStrategy
 */
public class ChaseClosestEntityStrategy implements EnemyStrategy {
    /**
     * {@inheritDoc}
     *
     * @param enemy {@inheritDoc}
     */
    @Override
    public void execute(Enemy enemy) {
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return;

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
        } else {
            new SeekFoodStrategy().execute(enemy);
        }
    }
}