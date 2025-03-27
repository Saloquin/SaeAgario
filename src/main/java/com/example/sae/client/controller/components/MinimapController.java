package com.example.sae.client.controller.components;

import com.example.sae.client.controller.SoloController;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

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
        // Called automatically by FXML loader
    }

    // Called manually from SoloController after loading the FXML
    public void setupMinimap() {
        minimapCanvas.setWidth(MINIMAP_SIZE);
        minimapCanvas.setHeight(MINIMAP_SIZE);

        // Create an animation loop to update the minimap regularly
        minimapUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawMinimap();
            }
        };
        minimapUpdater.start();
    }

    // This method draws the entire map and player on the minimap canvas
    private void drawMinimap() {
        Group root = SoloController.root;
        if (root == null || root.getChildren().isEmpty()) return;

        GraphicsContext gc = minimapCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, MINIMAP_SIZE, MINIMAP_SIZE);

        // Get world bounds
        double worldMinX = root.getBoundsInParent().getMinX();
        double worldMinY = root.getBoundsInParent().getMinY();
        double worldWidth = root.getBoundsInParent().getWidth();
        double worldHeight = root.getBoundsInParent().getHeight();

        // Scale for the minimap
        double scaleX = MINIMAP_SIZE / worldWidth;
        double scaleY = MINIMAP_SIZE / worldHeight;

        // Draw all entities on the minimap
        Set<Entity> entities = SoloController.getClient().getGameEngine().getEntities();

        for (Entity entity : entities) {
            if (!(entity instanceof Food)){
                double x = (entity.getSprite().getCenterX() - worldMinX) * scaleX;
                double y = (entity.getSprite().getCenterY() - worldMinY) * scaleY;
                double entitysizeX = entity.getMasse() * scaleX;
                double entitysizeY = entity.getMasse() * scaleY;

                gc.setFill(entity.getSprite().getFill());
                gc.fillOval(x - 2, y - 2, entitysizeX + 3, entitysizeY + 3);
            }

        }

        // Highlight the player's position in red
        var player = SoloController.getClient().getGameEngine().getPlayer(SoloController.getClient().getPlayerId());
        if (player != null) {
            double px = (player.getSprite().getCenterX() - worldMinX) * scaleX;
            double py = (player.getSprite().getCenterY() - worldMinY) * scaleY;
            double pxsize = player.getMasse() * scaleX;
            double pysize = player.getMasse() * scaleY;


            gc.setFill(Color.RED);
            gc.fillOval(px - 3, py - 3, pxsize, pysize);
        }
    }

    // Stop the animation timer when the game ends
    public void stop() {
        if (minimapUpdater != null) {
            minimapUpdater.stop();
        }
    }

    public AnchorPane getRoot() {
        return minimapRoot;
    }
}
