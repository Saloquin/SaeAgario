package com.example.sae.core;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.beans.binding.Bindings;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;

public class Camera extends Boundary {
    private ParallelCamera camera;
    private static final double BASE_ZOOM = 500.0;
    private static final double ZOOM_FACTOR = 100.0;
    private double currentScale = 1.0;
    private static final double DEFAULT_WIDTH = 1280;
    private static final double DEFAULT_HEIGHT = 720;

    public Camera() {
        super(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
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
                    return entity.Sprite.getCenterX() - width / 2;
                },
                entity.Sprite.centerXProperty()
        ));

        camera.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> {
                    Scene scene = entity.getScene();
                    double height = scene != null ? scene.getHeight() : DEFAULT_HEIGHT;
                    return entity.Sprite.getCenterY() - height / 2;
                },
                entity.Sprite.centerYProperty()
        ));

        updateBoundary(entity);
    }

    private void updateBoundary(Entity entity) {
        Scene scene = entity.getScene();
        double width = scene != null ? scene.getWidth() : DEFAULT_WIDTH;
        double height = scene != null ? scene.getHeight() : DEFAULT_HEIGHT;

        this.x = entity.Sprite.getCenterX() - width / 2;
        this.y = entity.Sprite.getCenterY() - height / 2;
        this.w = width;
        this.h = height;
    }

    public void adjustZoom(Entity entity) {
        // TODO: Implement zoom adjustment based on entity size
    }
}