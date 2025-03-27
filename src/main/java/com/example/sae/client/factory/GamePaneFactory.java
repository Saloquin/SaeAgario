package com.example.sae.client.factory;

import com.example.sae.client.utils.handler.MouseEventHandler;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class GamePaneFactory {
    public static Pane createGamePane(Group root, GameEngine gameEngine, int playerId, MouseEventHandler mouseHandler) {
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