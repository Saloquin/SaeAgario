package com.example.sae.enemyStrategy;

import com.example.sae.entity.Enemy;

import java.util.Random;

public class RandomMoveStrategy implements EnemyStrategy {
    private Random random = new Random();

    public void execute(Enemy enemy) {
        // Vérifiez si l'ennemi a une cible définie
        if (enemy.getTargetPosition() == null || enemy.hasReachedTarget()) {
            // Choisir un angle aléatoire pour la direction
            double angle = random.nextDouble() * 2 * Math.PI;

            // Calcul de la direction
            double[] direction = {Math.cos(angle), Math.sin(angle)};

            // Vitesse de déplacement (plus petite, pour un mouvement plus naturel)
            double speed = random.nextInt(100,300);

            // Nouvelle position cible
            double[] targetPosition = {
                    enemy.getPosition()[0] + direction[0] * speed,
                    enemy.getPosition()[1] + direction[1] * speed
            };

            // Définir la nouvelle cible
            enemy.setTargetPosition(targetPosition);

            // Déplacer l'ennemi vers la nouvelle position

        }
        enemy.moveToward(enemy.getTargetPosition());
    }


}

