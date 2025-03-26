package com.example.sae.core;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;

public class Camera extends Boundary {
    private static final double ZOOM_FACTOR = 6; // Increased to reduce zoom intensity


    public Camera() {
        super(0, 0, GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT);
    }

    public void focusOn(Entity entity) {
        if (entity == null || entity.getSprite() == null) return;

        Scene scene = entity.getSprite().getScene();
        if (scene == null) return;

        // Calculate final translation with offset
        double offsetX = scene.getWidth() / 2;
        double offsetY = scene.getHeight() / 2;

        scene.getRoot().translateXProperty().bind(
                entity.getSprite().centerXProperty().multiply(-1)
                        .multiply(Bindings.createDoubleBinding(
                                () -> 1.0 / (Math.sqrt(entity.getSprite().getRadius()) / ZOOM_FACTOR),
                                entity.getSprite().radiusProperty()
                        ))
                        .add(offsetX)
        );

        scene.getRoot().translateYProperty().bind(
                entity.getSprite().centerYProperty().multiply(-1)
                        .multiply(Bindings.createDoubleBinding(
                                () -> 1.0 / (Math.sqrt(entity.getSprite().getRadius()) / ZOOM_FACTOR),
                                entity.getSprite().radiusProperty()
                        ))
                        .add(offsetY)
        );

        // Handle zoom
        DoubleBinding zoomBinding = Bindings.createDoubleBinding(
                () -> 1.0 / (Math.sqrt(entity.getSprite().getRadius()) / ZOOM_FACTOR),
                entity.getSprite().radiusProperty()
        );

        scene.getRoot().scaleXProperty().bind(zoomBinding);
        scene.getRoot().scaleYProperty().bind(zoomBinding);
    }

}
