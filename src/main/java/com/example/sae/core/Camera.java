package com.example.sae.core;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Managed the view of the player
 */
public class Camera extends Boundary {
    public static final double ZOOM_FACTOR = 6; // Increased to reduce zoom intensity
    public static final double DEZOOM_FACTOR = 1.5;

    /**
     * Constructor of the class
     */
    public Camera() {
        super(0, 0, GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT);
    }

    /**
     * Focus the camera view on the entity
     * @param entity The entity on which the camera is focused
     */
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

    /**
     * bind a pane view to an entity on it
     *
     * @param pane   the pane to bind
     * @param entity the entity on which the pane is focused
     */
    public void focusPaneOn(Pane pane, Entity entity) {
        if (entity == null || entity.getSprite() == null) return;

        DoubleBinding zoomBinding = Bindings.createDoubleBinding(
                () -> 1.0 / ((Math.sqrt(entity.getSprite().getRadius()) / ZOOM_FACTOR)) * DEZOOM_FACTOR,
                entity.getSprite().radiusProperty()
        );

        pane.scaleXProperty().bind(zoomBinding);
        pane.scaleYProperty().bind(zoomBinding);

        pane.translateXProperty().bind(
                entity.getSprite().centerXProperty().subtract(Bindings.divide(pane.widthProperty(), 2)).negate().multiply(zoomBinding)
        );

        pane.translateYProperty().bind(
                entity.getSprite().centerYProperty().subtract(Bindings.divide(pane.heightProperty(), 2)).negate().multiply(zoomBinding)
        );
    }

}
