package com.example.sae.client;

import com.example.sae.client.factory.GamePaneFactory;
import com.example.sae.client.factory.GameSceneFactory;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class Client {
    protected GameEngine gameEngine;
    public int playerId;
    protected Group root;
    private Camera camera;
    protected boolean gameStarted = false;
    protected String playerName;
    protected Color color;

    public Client(Group root, String playerName, Color color) {
        this.root = root;
        this.playerName = playerName;
        this.color = color;
        camera = new Camera();
    }


    public boolean getGameStarted(){
        return gameStarted;
    }

    public abstract void init();
    public abstract void update();


    public Pane createGamePane(double width, double height) {
        return GamePaneFactory.createGamePane(root, gameEngine, playerId, width, height);
    }

    public Camera getCamera() {
        return camera;
    }

    public int getPlayerId() {
        return playerId;
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}