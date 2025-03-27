package com.example.sae.client;

import com.example.sae.client.utils.debug.DebugWindow;
import com.example.sae.client.utils.timer.GameTimer;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Player;
import com.example.sae.core.entity.powerUp.EffectManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;

/**
 *
 */
public class Solo extends Client {
    private final GameTimer gameTimer;
    private Player player;
    private final EffectManager effectManager;

    public Solo(Group root, String playerName, Color color) {
        super(root, playerName, color);
        this.gameTimer = new GameTimer(this);
        this.effectManager = new EffectManager();
    }

    @Override
    public void init() {
        gameStarted = true;
        player = EntityFactory.createPlayer(GameEngine.MASSE_INIT_PLAYER, playerName, color);
        playerId = gameEngine.addPlayer(player);
        if (DebugWindow.DEBUG_MODE) {
            DebugWindow.getInstance();
        }

        gameTimer.start();
    }

    @Override
    public void update() {
        Player player = gameEngine.getPlayer(playerId);
        if (player == null) {
            gameIsEnded.set(true);
        } else if (!gameIsEnded.get()) {
            manageEntities();
            effectManager.update();
            gameEngine.update();
            if (DebugWindow.DEBUG_MODE && DebugWindow.getInstance().getController() != null) {
                DebugWindow.getInstance().update(gameEngine, playerId);
            }
        }
    }

    @Override
    public void stopGame() {
        stopSoloGame();
    }

    public void stopSoloGame() {
        gameTimer.stop();
        if (!gameIsEnded.get()) {
            gameIsEnded.set(true);
        }
        gameEngine = null;
    }
}
