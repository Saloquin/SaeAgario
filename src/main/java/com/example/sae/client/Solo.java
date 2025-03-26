package com.example.sae.client;

import com.example.sae.client.debug.DebugWindow;
import com.example.sae.client.timer.GameTimer;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.Arrays;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Solo extends Client {
    private GameTimer gameTimer;
    private Player player;

    public Solo(Group root) {
        super(root);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
    }

    @Override
    public void init() {
        gameStarted = true;
<<<<<<< HEAD
        player = new Player(root, 5, Color.RED, true); // Joueur local
=======
        Player player = EntityFactory.createPlayer(3, Color.RED, true);
>>>>>>> main
        player.setCamera(camera);
        camera.focusOn(player);
        playerId = gameEngine.addPlayer(player);
        if(DebugWindow.DEBUG_MODE) {
            DebugWindow.getInstance();
        }
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
        if (gameEngine.getEntitiesOfType(Food.class).size() < GameEngine.NB_FOOD_MAX) {
            gameEngine.addEntity(EntityFactory.createFood(2));
        }

        if (gameEngine.getEntitiesOfType(Enemy.class).size() < GameEngine.NB_ENEMY_MAX) {
            gameEngine.addEntity(EntityFactory.createEnemy(5));
        }

        gameEngine.update();
        if (DebugWindow.DEBUG_MODE && DebugWindow.getInstance().getController() != null) {
            DebugWindow.getInstance().update(gameEngine, playerId);
        }

    }

    public void stopSoloGame() {
        gameTimer.stop();
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