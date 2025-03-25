package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
/**
 * AI strategy: move around to eat as much food as possible
 *
 * @see Enemy
 * @see ChaseClosestEntityStrategy
 * @see RandomMoveStrategy
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class SeekFoodStrategy implements EnemyStrategy {
    /**
     * Executes the AI's strategy of move around to eat as much food as possible
     *
     * @see Enemy
     * @see ChaseClosestEntityStrategy
     * @see RandomMoveStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param enemy Strategy executed on this AI
     */
    @Override
    public void execute(Enemy enemy) {
        GameEngine gameEngine = AgarioApplication.getClient().getGameEngine();
        if (gameEngine == null) return;

        double closestFoodDistance = Double.MAX_VALUE;
        Entity closestFood = null;

        for (Entity entity : gameEngine.getEntitiesOfType(Food.class)) {
            double distance = enemy.distanceTo(entity.getPosition());
            if (distance < closestFoodDistance) {
                closestFoodDistance = distance;
                closestFood = entity;
            }
        }

        if (closestFood != null) {
            enemy.moveToward(closestFood.getPosition());
        } else {
            new RandomMoveStrategy().execute(enemy);
        }
    }
}