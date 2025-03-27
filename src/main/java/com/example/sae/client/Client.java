package com.example.sae.client;

import com.example.sae.client.factory.GamePaneFactory;
import com.example.sae.client.utils.handler.MouseEventHandler;
import com.example.sae.client.utils.handler.MouseHandler;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public abstract class Client {
    protected GameEngine gameEngine;
    public int playerId;
    protected Group root;
    protected Camera camera;
    protected boolean gameStarted = false;
    protected MouseEventHandler mouseHandler;
    protected String playerName;
    protected Color color;

    public Client(Group root, String playerName, Color color) {
        this.root = root;
        this.playerName = playerName;
        this.color = color;
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

    public Pane createGamePane(double width, double height) {
        mouseHandler = new MouseHandler(root, gameEngine, playerId);
        return GamePaneFactory.createGamePane(root, gameEngine, playerId, camera, mouseHandler, width, height);
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