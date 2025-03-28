package com.example.sae.core.entity.movable.enemyStrategy;

import com.example.sae.client.Client;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.movable.Enemy;
import com.example.sae.core.entity.movable.body.MoveableBody;
import com.example.sae.core.entity.movable.Player;

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

        if (closestEntity != null && enemy.getComposite().getMainBody() == enemy) {
            enemy.updateTargetForClones(closestEntity.getPosition());
        }

        if (enemy.getTargetPosition() != null) {
            enemy.moveToward(enemy.getTargetPosition());
            return true;
        }

        return new SeekFoodStrategy().execute(enemy);
    }
}