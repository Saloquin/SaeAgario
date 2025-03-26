package com.example.sae.core;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.scene.ParallelCamera;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Camera extends Boundary {
    private ParallelCamera camera;
    private double currentZoom = 1;
    private double currentX = 0;
    private double currentY = 0;
    private static final double SMOOTHING = 0.1;
    private static final double ZOOM_FACTOR = 6; // Increased to reduce zoom intensity


    public Camera() {
        super(0, 0, GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT);
        camera = new ParallelCamera();
    }

    public ParallelCamera getCamera() {
        return camera;
    }

    public void focusOn(Entity entity) {
        if (entity == null || entity.getSprite() == null) return;

        Scene scene = entity.getSprite().getScene();
        if (scene == null) return;

        // Calculate target position
        double targetX = -entity.getSprite().getCenterX();
        double targetY = -entity.getSprite().getCenterY();

        // Smooth interpolation of position
        currentX += (targetX - currentX) * SMOOTHING;
        currentY += (targetY - currentY) * SMOOTHING;

        // Calculate final translation with offset
        double offsetX = scene.getWidth() / 2;
        double offsetY = scene.getHeight() / 2;

        scene.getRoot().setTranslateX(currentX * currentZoom + offsetX);
        scene.getRoot().setTranslateY(currentY * currentZoom + offsetY);

        // Handle zoom
        double radius = entity.getSprite().getRadius();
        double targetZoom = 1.0 / (Math.sqrt(radius) / ZOOM_FACTOR);
        currentZoom += (targetZoom - currentZoom) * SMOOTHING;

        scene.getRoot().setScaleX(currentZoom);
        scene.getRoot().setScaleY(currentZoom);
    }

    public void focusOn(Pane pane, Entity entity){
        pane.layoutXProperty().bind(entity.layoutXProperty());
        pane.layoutYProperty().bind(entity.layoutYProperty());
    }

}
