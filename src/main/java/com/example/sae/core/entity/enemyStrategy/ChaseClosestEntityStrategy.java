package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.client.controller.GameController;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.client.AgarioApplication;

public class ChaseClosestEntityStrategy implements EnemyStrategy {
    @Override
    public void execute(Enemy enemy) {
        GameEngine gameEngine = GameController.getClient().getGameEngine();
        if (gameEngine == null) return;

        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        // Find closest entity among existing entities
        for (Entity entity : gameEngine.getEntities()) {
            if (entity == enemy || entity.getParent() == null) continue;

            double distance = enemy.distanceTo(entity.getPosition());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }

        // Move toward closest entity if found
        if (closestEntity != null) {
            enemy.moveToward(closestEntity.getPosition());
        }
    }
}