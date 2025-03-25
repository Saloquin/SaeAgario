package com.example.sae.core;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.beans.binding.Bindings;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;

public class Camera extends Boundary {
    private ParallelCamera camera;
    private static final double BASE_ZOOM = 500.0;
    private static final double ZOOM_FACTOR = 100.0;
    private double currentScale = 1.0;
    private static final double DEFAULT_WIDTH = 1280;
    private static final double DEFAULT_HEIGHT = 720;

    private Rectangle visibleBounds;

    private static final double BUFFER = 0; // Réduit de 50 à 15 pixels

    // Rectangle plus petit que la caméra (petite zone visible)
    private static final double SMALL_RECTANGLE_WIDTH = 400.0;  // Largeur du petit rectangle
    private static final double SMALL_RECTANGLE_HEIGHT = 400.0; // Hauteur du petit rectangle

    public Rectangle getVisibleBounds() {
        if (visibleBounds == null) {
            visibleBounds = new Rectangle();
        }
        // Affichage de la zone visible complète
        visibleBounds.setX(x - BUFFER);
        visibleBounds.setY(y - BUFFER);
        visibleBounds.setWidth(w + BUFFER * 2);  // w est la largeur de la vue
        visibleBounds.setHeight(h + BUFFER * 2); // h est la hauteur de la vue
        return visibleBounds;
    }

    // Zone réduite (plus petite que la caméra)
    public Rectangle getSmallVisibleBounds() {
        if (visibleBounds == null) {
            visibleBounds = new Rectangle();
        }
        // Un petit rectangle plus petit que la zone de la caméra
        visibleBounds.setX(x - SMALL_RECTANGLE_WIDTH / 2);
        visibleBounds.setY(y - SMALL_RECTANGLE_HEIGHT / 2);
        visibleBounds.setWidth(SMALL_RECTANGLE_WIDTH);  // Largeur du petit rectangle
        visibleBounds.setHeight(SMALL_RECTANGLE_HEIGHT); // Hauteur du petit rectangle
        return visibleBounds;
    }

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
                    return entity.sprite.getCenterX() - width / 2;
                },
                entity.sprite.centerXProperty()
        ));

        camera.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> {
                    Scene scene = entity.getScene();
                    double height = scene != null ? scene.getHeight() : DEFAULT_HEIGHT;
                    return entity.sprite.getCenterY() - height / 2;
                },
                entity.sprite.centerYProperty()
        ));

        updateBoundary(entity);
    }

    private void updateBoundary(Entity entity) {
        Scene scene = entity.getScene();
        double width = scene != null ? scene.getWidth() : DEFAULT_WIDTH;
        double height = scene != null ? scene.getHeight() : DEFAULT_HEIGHT;

        this.x = entity.sprite.getCenterX() - width / 2;
        this.y = entity.sprite.getCenterY() - height / 2;
        this.w = width;
        this.h = height;
    }

    public void adjustZoom(Entity entity) {
        // TODO: Implémenter le zoom basé sur la taille de l'entité
    }
}
