package com.example.sae.client;

import com.example.sae.client.factory.GameSceneFactory;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.Scene;

public abstract class Client {
    protected GameEngine gameEngine;
    public int playerId;
    protected Group root;
    protected boolean gameStarted = false;

    public Client(Group root) {
        this.root = root;
    }


    public boolean getGameStarted(){
        return gameStarted;
    }

    public abstract void init();
    public abstract void update();

    public Scene createGameScene(double width, double height) {
        return GameSceneFactory.createGameScene(root, gameEngine, playerId, width, height);
    }


    public GameEngine getGameEngine() {
        return gameEngine;
    }
}