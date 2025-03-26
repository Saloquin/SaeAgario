package com.example.sae.client.debug;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.MoveableBody;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DebugWindowController {
    @FXML private VBox constantsBox;
    @FXML private Label foodCountLabel;
    @FXML private Label enemyCountLabel;
    @FXML private Label playerPositionLabel;
    @FXML private Label playerMassLabel;
    @FXML private ListView<String> logsListView;

    private static ObservableList<String> logs = FXCollections.observableArrayList();
    private static final int MAX_LOGS = 10;

    private Stage stage;

    private final IntegerProperty foodCount = new SimpleIntegerProperty(0);
    private final DoubleProperty maxFood = new SimpleDoubleProperty(0);
    private final IntegerProperty enemyCount = new SimpleIntegerProperty(0);
    private final DoubleProperty maxEnemy = new SimpleDoubleProperty(0);
    private final DoubleProperty playerX = new SimpleDoubleProperty(0);
    private final DoubleProperty playerY = new SimpleDoubleProperty(0);
    private final DoubleProperty playerMass = new SimpleDoubleProperty(0);

    public void initialize() {
        stage = new Stage(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        // Binding des labels avec les properties
        foodCountLabel.textProperty().bind(foodCount.asString().concat(" / ").concat(maxFood.asString()));
        enemyCountLabel.textProperty().bind(enemyCount.asString().concat(" / ").concat(maxEnemy.asString()));
        playerPositionLabel.textProperty().bind(
                playerX.asString("Position: (%.1f, ").concat(playerY.asString("%.1f)"))
        );
        playerMassLabel.textProperty().bind(playerMass.asString("Mass: %.1f"));

        // Initialisation des constantes
        addConstant("MAP_WIDTH", GameEngine.MAP_LIMIT_WIDTH);
        addConstant("MAP_HEIGHT", GameEngine.MAP_LIMIT_HEIGHT);
        addConstant("MAX_FOOD", GameEngine.NB_FOOD_MAX);
        addConstant("MAX_ENEMIES", GameEngine.NB_ENEMY_MAX);
        addConstant("BASE_MAX_SPEED", MoveableBody.BASE_MAX_SPEED);
        addConstant("MIN_MAX_SPEED", MoveableBody.MIN_MAX_SPEED);
        addConstant("SPEED_FACTOR", MoveableBody.SPEED_FACTOR);

        // Configuration de la ListView des logs
        logsListView.setItems(logs);
    }

    private void addConstant(String name, double value) {
        Label label = new Label(name + ": " + value);
        label.setStyle("-fx-text-fill: white;");
        constantsBox.getChildren().add(label);
    }

    public static void addLog(String message) {
        Platform.runLater(() -> {
            if (logs.size() >= MAX_LOGS) {
                logs.remove(0);
            }
            logs.add("[" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + message);
        });
    }

    // Getters pour les properties
    public IntegerProperty foodCountProperty() { return foodCount; }
    public DoubleProperty maxFoodProperty() { return maxFood; }
    public IntegerProperty enemyCountProperty() { return enemyCount; }
    public DoubleProperty maxEnemyProperty() { return maxEnemy; }
    public DoubleProperty playerXProperty() { return playerX; }
    public DoubleProperty playerYProperty() { return playerY; }
    public DoubleProperty playerMassProperty() { return playerMass; }
    public Stage getStage() { return stage; }
}