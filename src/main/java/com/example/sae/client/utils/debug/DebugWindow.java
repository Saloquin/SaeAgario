package com.example.sae.client.utils.debug;

import com.example.sae.client.controller.SoloController;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.movable.Enemy;
import com.example.sae.core.entity.immobile.Food;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class DebugWindow {
    public static final boolean DEBUG_MODE = false;
    private static DebugWindow instance;
    private DebugWindowController controller;

    private DebugWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/debug.fxml"));
            Scene scene = new Scene(loader.load());
            scene.setFill(null);

            controller = loader.getController();
            Stage stage = controller.getStage();
            stage.setScene(scene);
            stage.setX(10);
            stage.setY(10);

            // Définir les valeurs max
            controller.maxFoodProperty().set(GameEngine.NB_FOOD_MAX);
            controller.maxEnemyProperty().set(GameEngine.NB_ENEMY_MAX);

            if (DEBUG_MODE) {
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(GameEngine gameEngine, int playerId) {
        if (!DEBUG_MODE) return;

        var player = gameEngine.getPlayer(playerId);
        if (player != null) {
            controller.playerXProperty().set(player.getSprite().getCenterX());
            controller.playerYProperty().set(player.getSprite().getCenterY());
            controller.playerMassProperty().set(player.getMasse());
            controller.playerSpeedXProperty().set(player.getActualSpeedX());
            controller.playerSpeedYProperty().set(player.getActualSpeedY());
            controller.playerMaxSpeedProperty().set(player.getMaxSpeed());
        }

        // Utilise les méthodes statiques de MouseHandler pour mettre à jour la position
        controller.mouseXProperty().set(SoloController.getMousePosition()[0]);
        controller.mouseYProperty().set(SoloController.getMousePosition()[1]);

        controller.foodCountProperty().set(gameEngine.getEntitiesOfType(Food.class).size());
        controller.enemyCountProperty().set(gameEngine.getEntitiesOfType(Enemy.class).size());
    }

    public static DebugWindow getInstance() {
        if (instance == null && DEBUG_MODE) {
            instance = new DebugWindow();
        }
        return instance;
    }

    public DebugWindowController getController() {
        return controller;
    }
}