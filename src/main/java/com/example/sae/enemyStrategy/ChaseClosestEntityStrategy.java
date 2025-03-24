package com.example.sae.enemyStrategy;

import com.example.sae.AgarioApplication;
import com.example.sae.core.GameEngine;
import com.example.sae.entity.Enemy;
import com.example.sae.entity.Entity;


public class ChaseClosestEntityStrategy implements EnemyStrategy {
    @Override
    public void execute(Enemy enemy) {
        GameEngine gameEngine = AgarioApplication.getGameEngine();
        if (gameEngine == null) return;

        double closestEntityDistance = enemy.distanceTo(AgarioApplication.player.getPosition());
        Entity closestEntity = AgarioApplication.player;

        for (Entity entity : gameEngine.getEntitiesOfType(Enemy.class)) {
            if (entity == enemy) continue;

            double distance = enemy.distanceTo(entity.getPosition());
            if (distance < closestEntityDistance) {
                closestEntityDistance = distance;
                closestEntity = entity;
            }
        }

        enemy.moveToward(closestEntity.getPosition());
    }
}


