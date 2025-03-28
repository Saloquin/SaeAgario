package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.core.entity.Enemy;

/**
 * interface defining the mandatory method for an enemy strategy
 *
 * @see Enemy
 * @see ChaseClosestEntityStrategy
 * @see RandomMoveStrategy
 * @see SeekFoodStrategy
 */
public interface EnemyStrategy {
    /**
     * Execute the strategy
     *
     * @param enemy the enemy executing the strategy
     */
    void execute(Enemy enemy);
}

