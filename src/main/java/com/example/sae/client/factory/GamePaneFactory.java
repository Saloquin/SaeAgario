package com.example.sae.client.factory;

import com.example.sae.client.handler.MouseEventHandler;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

public class GamePaneFactory {
    public static Pane createGamePane(Group root, GameEngine gameEngine, int playerId, Camera camera, MouseEventHandler mouseHandler, double width, double height) {
        Pane pane = new Pane(root);
        pane.setOnMouseMoved(mouseHandler::handleMouseMove);
        pane.setOnMouseDragged(mouseHandler::handleMouseMove);
        pane.setOnMousePressed(mouseHandler::handleMouseMove);
        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                gameEngine.getPlayer(playerId).splitSprite();
            }
        });
        return pane;
    }
}