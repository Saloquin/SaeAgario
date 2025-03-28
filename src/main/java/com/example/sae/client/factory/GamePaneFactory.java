package com.example.sae.client.factory;

import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class GamePaneFactory {
    public static Pane createGamePane(Group root, GameEngine gameEngine, int playerId) {
        return new Pane(root);
    }
}