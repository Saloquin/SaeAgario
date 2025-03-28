package com.example.sae.client.controller.managers;

import com.example.sae.core.entity.movable.body.MoveableBody;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Supplier;

public class LeaderboardManager {
    private final ListView<String> leaderboard;
    private final Supplier<List<MoveableBody>> entitiesSupplier;
    private Timeline timeline;
    private ListCell<String> leaderboardCell;

    public LeaderboardManager(ListView<String> leaderboard, Supplier<List<MoveableBody>> entitiesSupplier) {
        this.leaderboard = leaderboard;
        this.entitiesSupplier = entitiesSupplier;
        initialize();
    }

    private void initialize() {

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void update() {
        leaderboard.getItems().clear();
        entitiesSupplier.get().stream()
                .filter(entity -> entity == entity.getComposite().getMainBody()) // Ne garde que les mainBody
                .forEach(entity -> leaderboard.getItems().add(
                        entity.getNom() + ": " + (int) Math.round(entity.getTotalMasse())
                ));
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}