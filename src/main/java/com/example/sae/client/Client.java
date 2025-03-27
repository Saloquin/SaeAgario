package com.example.sae.client;

import com.example.sae.client.factory.GamePaneFactory;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.powerUp.PowerUp;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * class that managed the Client
 */
public abstract class Client {
    /// The GameEngine
    protected static GameEngine gameEngine;
    /// The player's ID
    public int playerId;
    /// The group that contains the game
    protected Group root;
    /// The camera
    protected Camera camera;
    /// is the gameStarted ?
    protected boolean gameStarted = false;
    /// Player's name
    protected String playerName;
    /// Color of the player sprite
    protected Color color;

    /**
     * constructor
     * @param root the group that contains the game
     * @param playerName the name of the player
     * @param color the color of the sprite
     */
    public Client(Group root, String playerName, Color color) {
        this.root = root;
        this.playerName = playerName;
        this.color = color;
        this.camera = new Camera();
        this.gameEngine = new GameEngine(GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT, false);
    }

    /**
     * get the camera
     * @return The camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * return if the game is started or not
     * @return gameStarted
     */
    public boolean getGameStarted(){
        return gameStarted;
    }

    /**
     * First function called after the constructor
     */
    public abstract void init();

    /**
     * Called all functions that is needed to update the game
     */
    public abstract void update();

    /**
     * create the game pane
     * @return Pane
     */
    public Pane createGamePane() {
        return GamePaneFactory.createGamePane(root, gameEngine, playerId);
    }

    /**
     * get the player's ID
     * @return playerId
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * get the gameEngine
     * @return GameEngine
     */
    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    /**
     * managed the spawn of entities
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
}
