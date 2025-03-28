package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.Client;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.powerUp.PowerUp;

/**
 * Strategy making the enemy chase the closest food
 *
 * @see EnemyStrategy
 */
public class SeekFoodStrategy implements EnemyStrategy {

    /**
     * {@inheritDoc}
     *
     * @param enemy {@inheritDoc}
     */
    @Override
    public void execute(Enemy enemy) {
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return;

        double closestFoodDistance = Double.MAX_VALUE;
        Entity closestFood = null;

        for (Entity entity : gameEngine.getNearbyEntities(enemy, GameEngine.ENEMY_RANGE)) {
            if ((entity instanceof Food) || (entity instanceof PowerUp)) {
                if (entity.getMasse() * 1.33 < enemy.getMasse()) {
                    double distance = enemy.distanceTo(entity.getPosition());
                    if (distance < closestFoodDistance) {
                        closestFoodDistance = distance;
                        closestFood = entity;
                    }
                }
            }
        }

        if (closestFood != null) {
            enemy.moveToward(closestFood.getPosition());
        } else {
            new RandomMoveStrategy().execute(enemy);
        }
    }
}