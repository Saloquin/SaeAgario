package com.example.sae.server.debug;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerDebugWindow extends Application {
    private static GameEngine gameEngine;
    private Canvas canvas;
    private static final int WINDOW_SIZE = 800;
    private static final double SCALE = WINDOW_SIZE / GameEngine.MAP_LIMIT_WIDTH;
    private volatile boolean running = true;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(WINDOW_SIZE, WINDOW_SIZE);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);

        stage.setTitle("Server Debug View");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(e -> running = false);
        new Thread(this::updateLoop).start();
    }

    private void draw() {
        if (gameEngine == null) return;

        List<Entity> foods = new CopyOnWriteArrayList<>();
        List<Entity> players = new CopyOnWriteArrayList<>();

        synchronized (gameEngine) {
            try {
                foods.addAll(gameEngine.getEntitiesOfType(Food.class));
                players.addAll(gameEngine.getEntitiesOfType(Player.class));
            } catch (Exception e) {
                return;
            }
        }

        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);

            // Draw grid
            gc.setStroke(Color.DARKGRAY);
            gc.setLineWidth(0.5);
            int gridSize = 100;
            for (int i = 0; i <= GameEngine.MAP_LIMIT_WIDTH; i += gridSize) {
                double pos = i * SCALE;
                gc.strokeLine(pos, 0, pos, WINDOW_SIZE);
                gc.strokeLine(0, pos, WINDOW_SIZE, pos);
            }

            // Draw food
            gc.setFill(Color.YELLOW);
            for (Entity food : foods) {
                try {
                    double x = (food.getPosition()[0] + GameEngine.MAP_LIMIT_WIDTH/2) * SCALE;
                    double y = (food.getPosition()[1] + GameEngine.MAP_LIMIT_HEIGHT/2) * SCALE;
                    gc.fillOval(x - 2, y - 2, 4, 4);
                } catch (Exception e) {
                    continue;
                }
            }

            // Draw players
            for (Entity entity : players) {
                try {
                    Player player = (Player) entity;
                    // Convert from game coordinates (-1000 to 1000) to screen coordinates (0 to 800)
                    double x = (player.getPosition()[0] + GameEngine.MAP_LIMIT_WIDTH/2) * SCALE;
                    double y = (player.getPosition()[1] + GameEngine.MAP_LIMIT_HEIGHT/2) * SCALE;
                    double radius = Math.max(10, player.getMasse() * SCALE);

                    gc.setFill(player.getColor());
                    gc.fillOval(x - radius/2, y - radius/2, radius, radius);

                    // Draw player name
                    gc.setFill(Color.WHITE);
                    gc.fillText(player.getNom(), x - radius/2, y - radius/2 - 5);
                } catch (Exception e) {
                    continue;
                }
            }

            // Draw map center indicator
            gc.setStroke(Color.RED);
            gc.setLineWidth(1);
            double centerX = WINDOW_SIZE / 2;
            double centerY = WINDOW_SIZE / 2;
            gc.strokeLine(centerX - 10, centerY, centerX + 10, centerY);
            gc.strokeLine(centerX, centerY - 10, centerX, centerY + 10);
        });
    }

    private void updateLoop() {
        while (running) {
            try {
                draw();
                Thread.sleep(33);
            } catch (InterruptedException e) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void startDebugWindow(GameEngine engine) {
        gameEngine = engine;
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                ServerDebugWindow debugWindow = new ServerDebugWindow();
                debugWindow.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}