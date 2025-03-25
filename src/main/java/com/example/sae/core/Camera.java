package com.example.sae.core;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Camera extends Boundary {
    private ParallelCamera camera;
    private static final double BASE_ZOOM = 500.0;
    private double currentScale = 1.0;

    public Camera() {
        super(0, 0, GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT);
        camera = new ParallelCamera();
    }

    public ParallelCamera getCamera() {
        return camera;
    }

    public void focusOn(Entity entity) {
        camera.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> {
                    Scene scene = entity.getScene();
                    double width = scene != null ? scene.getWidth() : GameEngine.MAP_LIMIT_WIDTH;
                    return entity.Sprite.getCenterX() - width / 2;
                },
                entity.Sprite.centerXProperty()
        ));

        camera.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> {
                    Scene scene = entity.getScene();
                    double height = scene != null ? scene.getHeight() : GameEngine.MAP_LIMIT_HEIGHT;
                    return entity.Sprite.getCenterY() - height / 2;
                },
                entity.Sprite.centerYProperty()
        ));

        updateBoundary(entity);
    }

    public void adjustZoom(Entity entity) {
        // TODO: Implement zoom adjustment based on entity size
    }

    public void updateBoundary(Entity entity) {
        Scene scene = entity.getScene();
        double width = scene != null ? scene.getWidth() : GameEngine.MAP_LIMIT_WIDTH;
        double height = scene != null ? scene.getHeight() : GameEngine.MAP_LIMIT_HEIGHT;

        // Met à jour les coordonnées du Boundary pour le quadtree
        this.x = entity.getSprite().getCenterX() - width / (2 * currentScale);
        this.y = entity.getSprite().getCenterY() - height / (2 * currentScale);
        this.w = width / currentScale;
        this.h = height / currentScale;
    }
}