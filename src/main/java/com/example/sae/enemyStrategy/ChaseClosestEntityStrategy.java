package com.example.sae.enemyStrategy;

import com.example.sae.AgarioApplication;
import com.example.sae.entity.Enemy;
import com.example.sae.entity.Entity;


public class ChaseClosestEntityStrategy implements EnemyStrategy {
    @Override
    public void execute(Enemy enemy) {
        double closestEntityDistance = enemy.distanceTo(AgarioApplication.player.getPosition());
        Entity closestEntity = AgarioApplication.player;
        for (Entity entity : AgarioApplication.getNearbyEntities(enemy,1.5)) {
            if (entity instanceof Enemy) {
                if(entity.toString().equals(enemy.toString()))
                    continue;
                double distance = enemy.distanceTo(entity.getPosition());
                if (distance < closestEntityDistance) {
                    closestEntityDistance = distance;
                    closestEntity = entity;
                }
            }
        }

        enemy.moveToward(closestEntity.getPosition());
    }
}

