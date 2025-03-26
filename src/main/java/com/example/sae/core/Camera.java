package com.example.sae.core;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;

/**
 * Caméra 2D qui suit une entité (comme le joueur) dans un espace de jeu.
 * Applique un déplacement fluide au Group représentant le monde (ex: root).
 */
public class Camera extends Dimension2D {

    // Le groupe qui contient tous les éléments du monde (joueurs, nourriture, ennemis)
    private Group targetGroup;

    // L'entité à suivre (généralement le joueur)
    private Entity targetEntity;

    // Dimensions de la fenêtre (utilisées pour centrer le joueur)
    private final double screenWidth = AgarioApplication.getScreenWidth();
    private final double screenHeight = AgarioApplication.getScreenHeight();

    // Facteur de lissage : plus proche de 1.0 = mouvement lent, proche de 0 = rapide
    private final double smoothFactor = 0.1;

    // Timer pour mettre à jour la position de la caméra à chaque frame
    private final AnimationTimer cameraTimer;

    /**
     * Constructeur : initialise le timer de mise à jour
     */
    public Camera() {
        super(0,0);
        cameraTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (targetGroup != null && targetEntity != null) {
                    updateCamera(); // Mise à jour de la position du groupe
                }
            }
        };
    }

    /**
     * Définit le groupe à déplacer (le monde du jeu)
     * @param group Le Group (ex: root)
     */
    public void attachTo(Group group) {
        this.targetGroup = group;
    }

    /**
     * Active le suivi d'une entité (le joueur) et démarre la caméra
     * @param entity L'entité à suivre
     */
    public void focusOn(Entity entity) {
        this.targetEntity = entity;
        cameraTimer.start(); // Commence à suivre
    }

    /**
     * Calcule et applique la position de la caméra à chaque frame
     */
    private void updateCamera() {
        double centerOffset = targetEntity.Sprite.getRadius();
        double targetX = -targetEntity.getLayoutX() + screenWidth / 2 - centerOffset;
        double targetY = -targetEntity.getLayoutY() + screenHeight / 2 - centerOffset;

        double currentX = targetGroup.getTranslateX();
        double currentY = targetGroup.getTranslateY();

        double newX = currentX + (targetX - currentX) * smoothFactor;
        double newY = currentY + (targetY - currentY) * smoothFactor;

        targetGroup.setTranslateX(newX);
        targetGroup.setTranslateY(newY);
    }


    /**
     * Retourne la position actuelle de la caméra dans le monde
     * Utile pour par exemple dessiner un rectangle de vue sur une minimap
     * @return Décalage actuel en coordonnées du monde
     */
    public Point2D getWorldOffset() {
        if (targetGroup == null) return new Point2D(0, 0);
        return new Point2D(-targetGroup.getTranslateX(), -targetGroup.getTranslateY());
    }
}
