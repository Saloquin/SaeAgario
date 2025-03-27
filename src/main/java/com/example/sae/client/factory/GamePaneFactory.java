package com.example.sae.client.factory;

import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.Objects;

public class GamePaneFactory {
    /**
     *
     * @param root Contains the elements of the game
     * @param gameEngine the gameEngine that manage the game
     * @param playerId the players'id
     * @return A pane that contains all elements needed for the game for the players'id
     */
    public static Pane createGamePane(Group root, GameEngine gameEngine, int playerId) {
        Pane pane = new Pane(root);
        // Add Event when space is pressed, launch the split of the player
        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                gameEngine.getPlayer(playerId).splitSprite();
            }
        });
        return pane;
    }
    /**
     * {@return the name of the object}
     */
}