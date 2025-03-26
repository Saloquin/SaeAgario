package com.example.sae.client.controller.components;

import com.example.sae.client.controller.SoloController;
import com.example.sae.core.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import java.util.List;
import java.util.Set;

public class MinimapController {

    @FXML
    private AnchorPane minimapRoot;

    @FXML
    private Canvas minimapCanvas;

    private AnimationTimer minimapUpdater;

    private static final double MINIMAP_SIZE = 200;

    @FXML
    public void initialize() {
        // Nothing here
    }

    public void setupMinimap() {
        minimapCanvas.setWidth(MINIMAP_SIZE);
        minimapCanvas.setHeight(MINIMAP_SIZE);

        minimapUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawMinimap();
            }
        };
        minimapUpdater.start();
    }

    private void drawMinimap() {
        Group root = SoloController.root;
        if (root == null || root.getChildren().isEmpty()) return;

        GraphicsContext gc = minimapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE);

        double worldMinX = root.getBoundsInParent().getMinX();
        double worldMinY = root.getBoundsInParent().getMinY();
        double worldWidth = root.getBoundsInParent().getWidth();
        double worldHeight = root.getBoundsInParent().getHeight();

        double scaleX = MINIMAP_SIZE / worldWidth;
        double scaleY = MINIMAP_SIZE / worldHeight;

        // ✅ Use correct position from entity sprite
        Set<Entity> entities = SoloController.getClient().getGameEngine().getEntities();

        for (Entity entity : entities) {
            double x = (entity.getSprite().getCenterX() - worldMinX) * scaleX;
            double y = (entity.getSprite().getCenterY() - worldMinY) * scaleY;

            gc.setFill(entity.getSprite().getFill());
            gc.fillOval(x - 2, y - 2, 4, 4);
        }

        // 🔴 Highlight the player
        var player = SoloController.getClient().getGameEngine().getPlayer(SoloController.getClient().getPlayerId());
        if (player != null) {
            double px = (player.getSprite().getCenterX() - worldMinX) * scaleX;
            double py = (player.getSprite().getCenterY() - worldMinY) * scaleY;

            gc.setFill(Color.RED);
            gc.fillOval(px - 3, py - 3, 6, 6);
        }
    }



    public void stop() {
        if (minimapUpdater != null) {
            minimapUpdater.stop();
        }
    }

    public AnchorPane getRoot() {
        return minimapRoot;
    }
}
