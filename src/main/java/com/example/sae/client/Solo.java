package com.example.sae.client;

import com.example.sae.client.debug.DebugWindow;
import com.example.sae.client.timer.GameTimer;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.Arrays;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Solo extends Client {
    private GameTimer gameTimer;
    private Player player;


    public Solo(Group root, String name) {
        super(root, name);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);

    }

    @Override
    public void init() {
        gameStarted = true;
        System.out.println(playerName);
        player = EntityFactory.createPlayer(3, Color.BISQUE, true);
        player.setNom(playerName != null ? playerName : "Player");
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
}