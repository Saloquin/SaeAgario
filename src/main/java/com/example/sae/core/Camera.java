package com.example.sae.core;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;

public class Camera extends Boundary {
    private ParallelCamera camera;
    private static final double BASE_ZOOM = 500.0;
    private double currentScale = 1.0;
    private static final double DEFAULT_WIDTH = 1280;
    private static final double DEFAULT_HEIGHT = 720;

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
                    double width = scene != null ? scene.getWidth() : DEFAULT_WIDTH;
                    return entity.getSprite().getCenterX() - width / 2;
                },
                entity.getSprite().centerXProperty()
        ));

        camera.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> {
                    Scene scene = entity.getScene();
                    double height = scene != null ? scene.getHeight() : DEFAULT_HEIGHT;
                    return entity.getSprite().getCenterY() - height / 2;
                },
                entity.getSprite().centerYProperty()
        ));

        updateBoundary(entity);

        System.out.println("camera pos: " + camera.getTranslateX() + ", " + camera.getTranslateY());
        System.out.println("entity pos: " + entity.getTranslateX() + ", " + entity.getTranslateY());
    }

    private void updateBoundary(Entity entity) {
        Scene scene = entity.getScene();
        double width = scene != null ? scene.getWidth() : DEFAULT_WIDTH;
        double height = scene != null ? scene.getHeight() : DEFAULT_HEIGHT;

        this.x = entity.getSprite().getCenterX() - width / 2;
        this.y = entity.getSprite().getCenterY() - height / 2;
        this.w = width;
        this.h = height;
    }

    public void adjustZoom(Entity entity) {
        // TODO: Implémenter le zoom basé sur la taille de l'entité
    }

}
