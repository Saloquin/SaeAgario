package com.example.sae.client.factory;

import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;

public class GameSceneFactory {
    public static Scene createGameScene(Group root, GameEngine gameEngine, int playerId,double width, double height) {
        Scene scene = new Scene(root, width, height, Paint.valueOf("afafaf"));
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                gameEngine.getPlayer(playerId).splitSprite();
            }
        });
        return scene;
    }
}