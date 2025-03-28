package com.example.sae.client.factory;

import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

/**
 *
 */
public class GamePaneFactory {
    /**
     * Creates a game pane that contains all elements needed to render and interact with the game for the given player.
     *
     * @param root The root group containing all game elements
     * @param gameEngine The game engine that manages the game state and logic
     * @param playerId The ID of the player
     * @return The game pane containing the game scene for the specified player
     */
    public static Pane createGamePane(Group root, GameEngine gameEngine, int playerId) {
        return new Pane(root);
    }
    /**
     * {@return the name of the object}
     */
}