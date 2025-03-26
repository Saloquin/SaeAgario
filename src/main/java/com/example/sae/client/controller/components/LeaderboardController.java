package com.example.sae.client.controller.components;

import com.example.sae.core.entity.MoveableBody;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Supplier;

public class LeaderboardController {
    @FXML
    private ListView<String> leaderboard;
    private Timeline updateTimeline;
    private Supplier<List<MoveableBody>> entitiesSupplier;

    @FXML
    public void initialize() {
        // Cette méthode est appelée automatiquement par FXML
    }

    public void initializeWithSupplier(Supplier<List<MoveableBody>> entitiesSupplier) {
        this.entitiesSupplier = entitiesSupplier;
        this.updateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateLeaderboard()));
        this.updateTimeline.setCycleCount(Timeline.INDEFINITE);
        this.updateTimeline.play();
    }

    private void updateLeaderboard() {
        List<MoveableBody> sortedEntities = entitiesSupplier.get();
        leaderboard.getItems().clear();
        for (MoveableBody entity : sortedEntities) {
            leaderboard.getItems().add(entity.getNom() + ": " + (int) Math.round(entity.getMasse()));
        }
    }

    public void stop() {
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
    }
}