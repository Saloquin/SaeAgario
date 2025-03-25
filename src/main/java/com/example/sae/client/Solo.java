package com.example.sae.client;

import com.example.sae.client.controller.MenuController;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Solo extends Client {
    private GameTimer gameTimer;
    private int playerId;

    public Solo(Group root) {
        super(root);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
    }

    @Override
    public void init() {
        gameStarted = true;
        Player player = new Player(root, 3, Color.RED, true);
        player.setCamera(camera);
        camera.focusOn(player);
        playerId = gameEngine.addPlayer(player);
        gameTimer.start();
    }

    @Override
    public void update() {
        Player player = gameEngine.getPlayer(playerId);
        if (player == null) {
            new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(
                            javafx.util.Duration.seconds(2),
                            event -> returnToMenu()
                    )
            ).play();
            return;
        }

        player.setInputPosition(getMousePosition());
        if (gameEngine.getEntitiesOfType(Food.class).size() < 100) {
            gameEngine.addEntity(new Food(root, 2));
        }

        if (gameEngine.getEntitiesOfType(Enemy.class).size() < 10) {
            gameEngine.addEntity(new Enemy(root, 5));
        }


        gameEngine.update();
    }

    private static class GameTimer extends AnimationTimer {
        private final Solo client;
        private final double framesPerSecond = 120;
        private final double interval = 1000000000 / framesPerSecond;
        private double last = 0;

        public GameTimer(Solo client) {
            this.client = client;
        }

        @Override
        public void handle(long now) {
            if (last == 0) {
                last = now;
            }

            if (now - last > interval && client.gameStarted) {
                last = now;
                client.update();
            }
        }
    }

    private void returnToMenu() {
        Platform.exit();
    }
}