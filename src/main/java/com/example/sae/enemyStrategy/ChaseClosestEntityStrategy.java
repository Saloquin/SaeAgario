package com.example.sae.enemyStrategy;

import com.example.sae.AgarioApplication;
import com.example.sae.Enemy;
import com.example.sae.Entity;
import com.example.sae.MoveableBody;


public class ChaseClosestEntityStrategy implements EnemyStrategy {
    @Override
    public void execute(Enemy enemy) {
        double closestEntityDistance = enemy.distanceTo(AgarioApplication.player.getPosition());
        Entity closestEntity = AgarioApplication.player;
        for (Entity entity : AgarioApplication.getEntities(Enemy.class)) {
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

