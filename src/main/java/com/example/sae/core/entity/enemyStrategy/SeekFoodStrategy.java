package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;

public class SeekFoodStrategy implements EnemyStrategy {
    @Override
    public boolean execute(Enemy enemy) {
        GameEngine gameEngine = AgarioApplication.getClient().getGameEngine();
        if (gameEngine == null) return false;

        double closestFoodDistance = Double.MAX_VALUE;
        Entity closestFood = null;

        for (Entity entity : gameEngine.getNearbyEntities(enemy,400)) {
            if(!(entity instanceof Food)) {
                double distance = enemy.distanceTo(entity.getPosition());
                if (distance < closestFoodDistance) {
                    closestFoodDistance = distance;
                    closestFood = entity;
                }
            }
        }

        if (closestFood != null) {
            enemy.moveToward(closestFood.getPosition());
            return true;
        } else {
            return false;
        }
    }
}