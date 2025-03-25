package com.example.sae.core;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.quadtree.Boundary;
import javafx.beans.binding.Bindings;
import javafx.scene.ParallelCamera;

public class Camera extends Boundary {
    private ParallelCamera camera;
    private static final double BASE_ZOOM = 500.0;
    private static final double ZOOM_FACTOR = 100.0;
    private double currentScale = 1.0;

    public Camera() {
        super(0, 0, AgarioApplication.getScreenWidth(), AgarioApplication.getScreenHeight());
        camera = new ParallelCamera();
    }

    public ParallelCamera getCamera() {
        return camera;
    }

    public void focusOn(Entity entity) {
        camera.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> entity.Sprite.getCenterX() - AgarioApplication.getScreenWidth() / 2,
                entity.Sprite.centerXProperty()
        ));
        camera.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> entity.Sprite.getCenterY() - AgarioApplication.getScreenHeight() / 2,
                entity.Sprite.centerYProperty()
        ));

        // Update boundary position
        this.x = entity.Sprite.getCenterX() - AgarioApplication.getScreenWidth() / 2;
        this.y = entity.Sprite.getCenterY() - AgarioApplication.getScreenHeight() / 2;
    }

    public void adjustZoom(Entity entity) {
        //TODO();
    }
}