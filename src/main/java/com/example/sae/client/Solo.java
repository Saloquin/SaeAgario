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
 * Manages the game logic when the player is playing in offline (solo) mode.
 * Extends of {@link Client}
 */

public class Solo extends Client {
    /// The timer handling game updates
    private GameTimer gameTimer;
    /// the player controlled in solo mode
    private Player player;

    /// Handles effects from power-ups
    private EffectManager effectManager;

    /// Property indicating whether the game has ended

    private final BooleanProperty gameIsEnded = new SimpleBooleanProperty(false);

    /**
     * Constructor of the class
     * @param root The root group that contains the game content
     * @param playerName The name of the player
     * @param color The color of the player's sprite
     */
    public Solo(Group root, String playerName, Color color) {
        super(root, playerName, color);
        this.gameTimer = new GameTimer(this);
        this.effectManager = new EffectManager();
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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

    /**
     * Returns the property indicating whether the game has ended.
     * @return The {@link BooleanProperty} representing the game-ended state
     */
    public BooleanProperty getGameIsEndedProperty() {
        return gameIsEnded;
    }

    /**
     * Stop the game
     */
    public void stopSoloGame() {
        gameTimer.stop();
        if (!gameIsEnded.get()) {
            gameIsEnded.set(true);
        }
        gameEngine = null;
    }
}
