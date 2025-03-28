package com.example.sae.core.entity.movable.enemyStrategy;

import com.example.sae.client.Client;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.movable.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.immobile.Food;
import com.example.sae.core.entity.immobile.powerUp.PowerUp;

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
     * @return will return true if execution was successful
     */
    @Override
    public boolean execute(Enemy enemy) {
        GameEngine gameEngine = Client.getGameEngine();
        if (gameEngine == null) return false;

        double closestFoodDistance = Double.MAX_VALUE;
        Entity closestFood = null;

        for (Entity entity : gameEngine.getNearbyEntities(enemy,GameEngine.ENEMY_RANGE)) {
            if((entity instanceof Food) || (entity instanceof PowerUp)) {
                if(gameEngine.canEat(enemy,entity)) {
                    double distance = enemy.distanceTo(entity.getPosition());
                    if (distance < closestFoodDistance) {
                        closestFoodDistance = distance;
                        closestFood = entity;
                    }
                }
            }
        }

        if (closestFood != null && enemy.getComposite().getMainBody() == enemy) {
            enemy.updateTargetForClones(closestFood.getPosition());
        }

        if (enemy.getTargetPosition() != null) {
            enemy.moveToward(enemy.getTargetPosition());
            return true;
        }

        return new RandomMoveStrategy().execute(enemy);
    }
}