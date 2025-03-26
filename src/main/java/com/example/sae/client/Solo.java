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
import javafx.stage.Stage;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Solo extends Client {
    private GameTimer gameTimer;

    public Solo(Group root) {
        super(root);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
    }

    @Override
    public void init() {
        gameStarted = true;
        Player player = EntityFactory.createPlayer(3, Color.RED, true);
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

    private void returnToMenu() {
        Platform.exit();
    }
}