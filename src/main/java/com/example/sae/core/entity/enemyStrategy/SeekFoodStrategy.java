package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.Client;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.powerUp.PowerUp;

/**
 * AI strategy: move around to eat as much food as possible
 *
 * @see Enemy
 * @see ChaseClosestEntityStrategy
 * @see RandomMoveStrategy
 */
public class SeekFoodStrategy implements EnemyStrategy {
    /**
     * Executes the AI's strategy of move around to eat as much food as possible
     *
     * @param enemy Strategy executed on this AI
     * @return will return true if execution was successful
     * @see Enemy
     * @see ChaseClosestEntityStrategy
     * @see RandomMoveStrategy
     */
    @Override
    public boolean execute(Enemy enemy) {
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return false;

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
            return true;
        } else {
            new RandomMoveStrategy().execute(enemy);
            return false;
        }
    }
}