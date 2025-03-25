package com.example.sae.client;

import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Player;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;


public abstract class Client {
    protected GameEngine gameEngine;
    public int playerId;
    protected Point2D mousePosition = new Point2D(0, 0);
    protected boolean mouseMoved = false;
    protected Group root;
    protected Camera camera;
    protected boolean gameStarted = false;

    public Client(Group root) {
        this.root = root;
        this.camera = new Camera();
    }

    public Camera getCamera() {
        return camera;
    }

    public abstract void init();
    public abstract void update();

    protected void handleMouseMove(MouseEvent e) {
        Point2D localPoint = root.screenToLocal(e.getScreenX(), e.getScreenY());
        if (localPoint != null) {
            mousePosition = localPoint;
            mouseMoved = true;
        }
    }

    public Scene createGameScene(double width, double height) {
        Scene scene = new Scene(root, width, height, Paint.valueOf("afafaf"));
        scene.setOnMouseMoved(this::handleMouseMove);
        scene.setOnMouseDragged(this::handleMouseMove);
        scene.setOnMousePressed(this::handleMouseMove);
        scene.setCamera(camera.getCamera());
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                gameEngine.getPlayer(playerId).splitSprite();
            }
        });
        return scene;
    }

    public double[] getMousePosition() {
        if (!mouseMoved) {
            Scene scene = root.getScene();
            if (scene != null) {
                return new double[]{scene.getWidth() / 2, scene.getHeight() / 2};
            }
            return new double[]{0, 0}; // Fallback if scene is not available
        }
        return new double[]{mousePosition.getX(), mousePosition.getY()};
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}