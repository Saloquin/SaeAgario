package com.example.sae.client.utils.debug;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Food;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.example.sae.core.entity.MoveableBody.BASE_MAX_SPEED;
import static com.example.sae.core.entity.MoveableBody.MIN_MAX_SPEED;

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
            double speed = BASE_MAX_SPEED / (1 + Math.log10(player.getMasse()));
            speed = Math.max(speed, MIN_MAX_SPEED);
            controller.playerSpeedProperty().set(speed);
        }

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