package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.core.entity.Enemy;

/**
 * interface for different AI strategies
 *
 * @see Enemy
 * @see ChaseClosestEntityStrategy
 * @see RandomMoveStrategy
 * @see SeekFoodStrategy
 */
public interface EnemyStrategy {
    /**
     * Executes the AI's strategy
     *
     * @param enemy Strategy executed on this AI
     * @return will return true if execution was successful
     * @see Enemy
     * @see ChaseClosestEntityStrategy
     * @see RandomMoveStrategy
     * @see SeekFoodStrategy
     */
    boolean execute(Enemy enemy);
}

