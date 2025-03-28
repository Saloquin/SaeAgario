package com.example.sae.client;

import com.example.sae.client.factory.GamePaneFactory;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.powerUp.PowerUp;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Abstract base class that manages the game client logic.
 */
public abstract class Client {
    /// The game engine that handles entity updates and game logic
    protected static GameEngine gameEngine;

    /// property updating when the game end
    protected final BooleanProperty gameIsEnded = new SimpleBooleanProperty(false);

    /// The player's id
    public int playerId;

    /// The root group containing all visual elements of the game
    protected Group root;

    /// The camera used to follow the player and control view scaling
    protected Camera camera;

    /// Indicates whether the game has started
    protected boolean gameStarted = false;

    /// The player's name
    protected String playerName;

    /// The player's color used to display their sprite
    protected Color color;

    /**
     * Constructs a new Client.
     *
     * @param root The root group containing the game content
     * @param playerName The name of the player
     * @param color The color of the player's sprite
     */
    public Client(Group root, String playerName, Color color) {
        this.root = root;
        this.playerName = playerName;
        this.color = color;
        this.camera = new Camera();
        this.gameEngine = new GameEngine(false);
    }

    /**
     * Returns the game camera.
     *
     * @return The camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * Returns whether the game has started.
     *
     * @return {@code true} if the game has started, {@code false} otherwise
     */
    public boolean getGameStarted() {
        return gameStarted;
    }

    /**
     * Called after construction to initialize the game.
     */
    public abstract void init();

    /**
     * Updates the game state.
     */
    public abstract void update();

    /**
     * Creates and returns the game pane associated with this client.
     *
     * @return A {@link Pane} that contains the game view
     */
    public Pane createGamePane() {
        return GamePaneFactory.createGamePane(root, gameEngine, playerId);
    }

    /**
     * {@return The player's ID}
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * {@return The game engine}
     */
    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    /**
     * Manages entity spawning.
     */
    protected void manageEntities() {
        if (gameEngine.getEntitiesOfType(Enemy.class).size() < GameEngine.NB_ENEMY_MAX) {
            gameEngine.addEntity(EntityFactory.createEnemy(GameEngine.MASSE_INIT_ENEMY));
        }
        if (gameEngine.getEntitiesOfType(Food.class).size() < GameEngine.NB_FOOD_MAX) {
            gameEngine.addEntity(EntityFactory.createFood(GameEngine.MASSE_INIT_FOOD));
        }
        if (gameEngine.getEntitiesOfType(PowerUp.class).size() < GameEngine.NB_POWERUP_MAX) {
            gameEngine.addEntity(EntityFactory.createRandomPowerUp());
        }
    }

    public abstract void stopGame();
}

