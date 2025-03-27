package com.example.sae.client;

import com.example.sae.client.factory.GamePaneFactory;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Food;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class Client {
    protected GameEngine gameEngine;
    public int playerId;
    protected Group root;
    protected Camera camera;
    protected boolean gameStarted = false;
    protected String playerName;
    protected Color color;

    public Client(Group root, String playerName, Color color) {
        this.root = root;
        this.playerName = playerName;
        this.color = color;
        this.camera = new Camera();
        this.gameEngine = new GameEngine(GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT, false);
    }

    public Camera getCamera() {
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

    public GameEngine getGameEngine() {
        return gameEngine;
    }

    protected void manageEntities() {
        if (gameEngine.getEntitiesOfType(Food.class).size() < GameEngine.NB_FOOD_MAX) {
            gameEngine.addEntity(EntityFactory.createFood(GameEngine.MASSE_INIT_FOOD));
        }

        if (gameEngine.getEntitiesOfType(Enemy.class).size() < GameEngine.NB_ENEMY_MAX) {
            gameEngine.addEntity(EntityFactory.createEnemy(GameEngine.MASSE_INIT_ENEMY));
        }
    }
}
