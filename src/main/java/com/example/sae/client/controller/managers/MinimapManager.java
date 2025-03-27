package com.example.sae.client.controller.managers;

import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.MoveableBody;
import com.example.sae.core.entity.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Supplier;

public class MinimapManager {
    private static final double WORLD_SIZE = 4000;
    private static final double MAX_MASS = 160000;
    private static final double MARGIN = 0.1;

    private final GraphicsContext gc;
    private final Canvas canvas;
    private final Player player;
    private final Supplier<List<MoveableBody>> entitiesSupplier;

    public MinimapManager(Canvas canvas, Player player, Supplier<List<MoveableBody>> entitiesSupplier) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.player = player;
        this.entitiesSupplier = entitiesSupplier;
        initialize();
    }

    private void initialize() {
        gc.setFill(Color.rgb(30, 30, 40));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Timeline updater = new Timeline(
                new KeyFrame(Duration.millis(100), e -> update()));
        updater.setCycleCount(Timeline.INDEFINITE);
        updater.play();
    }

    private void update() {
        clear();
        drawWorld();
        drawEntities();
        drawPlayer();
        drawCenter();
    }

    private void clear() {
        gc.setFill(Color.rgb(30, 30, 40));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void drawWorld() {
        double scale = calculateScale();
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        gc.setStroke(Color.rgb(100, 100, 120));
        gc.setLineWidth(1);
        gc.strokeRect(
                centerX - (WORLD_SIZE/2 * scale),
                centerY - (WORLD_SIZE/2 * scale),
                WORLD_SIZE * scale,
                WORLD_SIZE * scale
        );
    }

    private void drawEntities() {
        double scale = calculateScale();
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        for (MoveableBody entity : entitiesSupplier.get()) {
            double x = centerX + (entity.getX() * scale);
            double y = centerY + (entity.getY() * scale);
            double size = Math.max(2, Math.sqrt(entity.getMasse() / MAX_MASS) * 20);

            gc.setFill(entity instanceof Player ? Color.CYAN :
                    entity instanceof Enemy ? Color.RED : Color.GREEN);
            gc.fillOval(x - size/2, y - size/2, size, size);
        }
    }

    private void drawPlayer() {
        double scale = calculateScale();
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        double x = centerX + (player.getX() * scale);
        double y = centerY + (player.getY() * scale);
        double size = Math.max(5, Math.sqrt(player.getMasse() / MAX_MASS) * 25);

        gc.setFill(Color.YELLOW);
        gc.fillOval(x - size/2, y - size/2, size, size);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.strokeOval(x - size/2, y - size/2, size, size);
    }

    private void drawCenter() {
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double crossSize = 5;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(0.5);
        gc.strokeLine(centerX - crossSize, centerY, centerX + crossSize, centerY);
        gc.strokeLine(centerX, centerY - crossSize, centerX, centerY + crossSize);
    }

    private double calculateScale() {
        double scaleX = canvas.getWidth() / (WORLD_SIZE * (1 + MARGIN));
        double scaleY = canvas.getHeight() / (WORLD_SIZE * (1 + MARGIN));
        return Math.min(scaleX, scaleY);
    }
}