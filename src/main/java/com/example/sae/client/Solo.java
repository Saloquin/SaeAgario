package com.example.sae.client;

import com.example.sae.client.debug.DebugWindow;
import com.example.sae.client.timer.GameTimer;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import com.example.sae.core.entity.powerUp.EffectManager;
import com.example.sae.core.entity.powerUp.PowerUp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Solo extends Client {
    private GameTimer gameTimer;
    private Player player;

    private EffectManager effectManager;
    private final BooleanProperty gameIsEnded = new SimpleBooleanProperty(false);

    public Solo(Group root) {
        super(root);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
        this.effectManager = new EffectManager();
    }

    @Override
    public void init() {
        gameStarted = true;
        player = EntityFactory.createPlayer(10, Color.BISQUE, true);
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
            gameIsEnded.set(true);
        }
        else{
            player.setInputPosition(getMousePosition());
            if (gameEngine.getEntitiesOfType(Food.class).size() < GameEngine.NB_FOOD_MAX) {
                gameEngine.addEntity(EntityFactory.createFood(2));
            }
            if (gameEngine.getEntitiesOfType(PowerUp.class).size() < GameEngine.NB_POWERUP_MAX) {
                gameEngine.addEntity(EntityFactory.createRandomPowerUp());
            }

            if (gameEngine.getEntitiesOfType(Enemy.class).size() < GameEngine.NB_ENEMY_MAX) {
                gameEngine.addEntity(EntityFactory.createEnemy(10));
            }
            effectManager.update();
            gameEngine.update();
            if (DebugWindow.DEBUG_MODE && DebugWindow.getInstance().getController() != null) {
                DebugWindow.getInstance().update(gameEngine, playerId);
            }
        }


    }

    public BooleanProperty getGameIsEndedProperty() {
        return gameIsEnded;
    }

    public void stopSoloGame() {
        gameTimer.stop();
    }
}