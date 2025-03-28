package com.example.sae.client;

import com.example.sae.client.controller.managers.MinimapManager;
import com.example.sae.client.controller.managers.PlayerInfoManager;
import com.example.sae.client.factory.GamePaneFactory;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.movable.Enemy;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.immobile.Food;
import com.example.sae.core.entity.immobile.powerUp.PowerUp;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;

public abstract class Client {
    protected static GameEngine gameEngine;
    public int playerId;
    protected Group root;
    protected static Camera camera;
    protected boolean gameStarted = false;
    protected String playerName;
    protected Color color;
    protected static final BooleanProperty gameIsEnded = new SimpleBooleanProperty(false);

    public Client(Group root, String playerName, Color color) {
        this.root = root;
        this.playerName = playerName;
        this.color = color;
        camera = new Camera();
        gameEngine = new GameEngine(GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT, false);
    }

    public static Camera getCamera() {
        return camera;
    }

    public boolean getGameStarted(){
        return gameStarted;
    }

    public abstract void init();
    public abstract void update();

    public Pane createGamePane() {
        return GamePaneFactory.createGamePane(root, gameEngine, playerId);
    }


    public int getPlayerId() {
        return playerId;
    }

    public static GameEngine getGameEngine() {
        return gameEngine;
    }

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
