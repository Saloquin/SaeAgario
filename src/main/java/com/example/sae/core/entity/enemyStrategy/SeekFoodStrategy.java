package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import com.example.sae.client.AgarioApplication;

public class SeekFoodStrategy implements EnemyStrategy {
    @Override
    public void execute(Enemy enemy) {
        GameEngine gameEngine = AgarioApplication.getGameEngine();
        if (gameEngine == null) return;

        double closestFoodDistance = Double.MAX_VALUE;
        Entity closestFood = null;

        // Utiliser getEntitiesOfType de GameEngine pour obtenir la nourriture
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
