package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.Client;
import com.example.sae.client.controller.SoloController;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.*;
import com.example.sae.core.entity.powerUp.PowerUp;

import java.util.HashSet;

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
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return false;

        MoveableBody closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        HashSet<Entity> nearbyEntities = gameEngine.getNearbyEntities(enemy, GameEngine.ENEMY_RANGE);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Enemy || entity instanceof Player) {
                if (gameEngine.canEat(enemy, entity)) {
                    double distance = enemy.distanceTo(entity.getPosition());
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestEntity = (MoveableBody) entity;
                    }
                }
            }
        }

        if (closestEntity != null) {
            enemy.moveToward(closestEntity.getPosition());
            return true;
        }
        else {
            new SeekFoodStrategy().execute(enemy);
            return false;
        }
    }
}