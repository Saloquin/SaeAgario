package com.example.sae.core.entity.enemyStrategy;

import com.example.sae.core.entity.Enemy;
/**
 * interface for different AI strategies
 *
 * @see Enemy
 * @see ChaseClosestEntityStrategy
 * @see RandomMoveStrategy
 * @see SeekFoodStrategy
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public interface EnemyStrategy {
    /**
     * Executes the AI's strategy
     *
     * @see Enemy
     * @see ChaseClosestEntityStrategy
     * @see RandomMoveStrategy
     * @see SeekFoodStrategy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param enemy Strategy executed on this AI
     */
    boolean execute(Enemy enemy);
}

