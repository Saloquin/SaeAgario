package com.example.sae.client;

import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Player;
import javafx.animation.AnimationTimer;
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
    protected Point2D lastValidMousePosition = new Point2D(0, 0);  // Add this field

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
    protected double[] direction = new double[]{0, 0};

    protected void handleMouseMove(MouseEvent e) {
        Point2D localPoint = root.screenToLocal(e.getScreenX(), e.getScreenY());
        if (localPoint != null) {
            mousePosition = localPoint;
            lastValidMousePosition = localPoint;

            // Calculer le vecteur direction
            Player player = gameEngine.getPlayer(playerId);
            if (player != null) {
                double dx = localPoint.getX() - player.getSprite().getCenterX();
                double dy = localPoint.getY() - player.getSprite().getCenterY();
                double length = Math.sqrt(dx * dx + dy * dy);
                if (length > 0) {
                    direction[0] = dx / length;
                    direction[1] = dy / length;
                }
            }
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
        Player player = gameEngine.getPlayer(playerId);
        if (player != null) {
            // Utiliser le vecteur direction pour calculer la prochaine position
            double nextX = player.getSprite().getCenterX() + direction[0] * 100;
            double nextY = player.getSprite().getCenterY() + direction[1] * 100;
            return new double[]{nextX, nextY};
        }
        return new double[]{lastValidMousePosition.getX(), lastValidMousePosition.getY()};
    }

    public GameEngine getGameEngine() {
        return gameEngine;
    }
}