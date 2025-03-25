package com.example.sae.client.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;


public class GameController {
    @FXML
    private Canvas minimap;

    @FXML
    private ListView<String> leaderboard;

    @FXML
    private ListView<String> listChat;

    @FXML
    private TextField sendMessage;

    @FXML
    public AnchorPane hudContainer;

    @FXML
    public Pane gameContainer;

    public Pane getGameContainer() {
        return gameContainer;
    }

    private final double WORLD_WIDTH = 1280;
    private final double WORLD_HEIGHT = 720;

    @FXML
    public void initialize() {
        // Boucle d'animation : juste pour redessiner la minimap régulièrement
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawMinimap();
            }
        }.start();
    }

    private void drawMinimap() {
        GraphicsContext gc = minimap.getGraphicsContext2D();
        gc.clearRect(0, 0, minimap.getWidth(), minimap.getHeight());

        double scaleX = minimap.getWidth() / WORLD_WIDTH;
        double scaleY = minimap.getHeight() / WORLD_HEIGHT;

        // Fond
        gc.setFill(Color.rgb(240, 240, 240, 0.9));
        gc.fillRect(0, 0, minimap.getWidth(), minimap.getHeight());

        // 🔴 Exemple : entité fixe rouge à (1000, 1000)
        drawEntity(gc, 1000, 450, 30, Color.RED, scaleX, scaleY);

        // 🔵 Exemple : entité bleue à (400, 300)
        drawEntity(gc, 400, 300, 20, Color.BLUE, scaleX, scaleY);

        // 🟢 Exemple : entité verte à (1500, 1200)
        drawEntity(gc, 1000, 500, 25, Color.GREEN, scaleX, scaleY);
    }

    private void drawEntity(GraphicsContext gc, double worldX, double worldY, double radius,
                            Color color, double scaleX, double scaleY) {
        double x = worldX * scaleX;
        double y = worldY * scaleY;
        double r = radius * scaleX;

        gc.setFill(color);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        gc.fillOval(x - r, y - r, r * 2, r * 2);
        gc.strokeOval(x - r, y - r, r * 2, r * 2);
    }
}
