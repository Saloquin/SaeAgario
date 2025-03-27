package com.example.sae.client.utils.handler;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Player;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

public class MouseHandler implements MouseEventHandler {
    private Point2D mousePosition = new Point2D(0, 0);
    private Point2D lastValidMousePosition = new Point2D(0, 0);
    private double[] direction = new double[]{0, 0};
    private Parent root;
    private GameEngine gameEngine;
    private int playerId;

    public MouseHandler(Parent root, GameEngine gameEngine, int playerId) {
        this.root = root;
        this.gameEngine = gameEngine;
        this.playerId = playerId;
    }

    @Override
    public void handleMouseMove(MouseEvent e) {
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

    @Override
    public double[] getMousePosition() {
        Player player = gameEngine.getPlayer(playerId);
        if (player != null) {
            double nextX = player.getSprite().getCenterX() + direction[0] * 100;
            double nextY = player.getSprite().getCenterY() + direction[1] * 100;
            return new double[]{nextX, nextY};
        }
        return new double[]{lastValidMousePosition.getX(), lastValidMousePosition.getY()};
    }
}