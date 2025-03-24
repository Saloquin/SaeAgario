package com.example.sae.enemyStrategy;

import com.example.sae.entity.Enemy;
import com.example.sae.entity.Entity;
import com.example.sae.entity.Food;
import com.example.sae.AgarioApplication;

public class SeekFoodStrategy implements EnemyStrategy {
    @Override
    public void execute(Enemy enemy) {
        double closestFoodDistance = Double.MAX_VALUE;
        Entity closestFood = null;

        for (Entity entity : AgarioApplication.getNearbyEntities(enemy,2.5)) {
            if (entity instanceof Food) {
                double distance = enemy.distanceTo(entity.getPosition());
                if (distance < closestFoodDistance) {
                    closestFoodDistance = distance;
                    closestFood = entity;
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

