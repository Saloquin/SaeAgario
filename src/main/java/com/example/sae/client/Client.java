package com.example.sae.client;

import com.example.sae.client.factory.GameSceneFactory;
import com.example.sae.client.handler.MouseEventHandler;
import com.example.sae.client.handler.MouseHandler;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.Scene;

public abstract class Client {
    protected GameEngine gameEngine;
    public int playerId;
    protected Group root;
    protected Camera camera;
    protected boolean gameStarted = false;
    protected MouseEventHandler mouseHandler;

    public Client(Group root) {
        this.root = root;
        this.camera = new Camera();
    }

    public Camera getCamera() {
        return camera;
    }

    public boolean getGameStarted(){
        return gameStarted;
    }

    public abstract void init();
    public abstract void update();

    public Scene createGameScene(double width, double height) {
        mouseHandler = new MouseHandler(root, gameEngine, playerId);
        return GameSceneFactory.createGameScene(root, gameEngine, playerId, camera, mouseHandler, width, height);
    }

    public double[] getMousePosition() {
        return mouseHandler.getMousePosition();
    }

    public int getPlayerId() {
        return playerId;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}