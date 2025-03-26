package com.example.sae.client.controller.components;

import com.example.sae.core.entity.MoveableBody;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Supplier;

public class Leaderboard {
    private final ListView<String> leaderboard;
    private final Timeline updateTimeline;
    private final Supplier<List<MoveableBody>> entitiesSupplier;

    public Leaderboard(ListView<String> leaderboard, Supplier<List<MoveableBody>> entitiesSupplier) {
        this.leaderboard = leaderboard;
        this.entitiesSupplier = entitiesSupplier;
        this.updateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateLeaderboard()));
        this.updateTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        updateTimeline.play();
    }

    public void stop() {
        updateTimeline.stop();
    }

    public void updateLeaderboard() {
        leaderboard.getItems().clear();
        for (MoveableBody entity : entitiesSupplier.get()) {
            leaderboard.getItems().add(entity.getNom() + ": " + (int) Math.round(entity.getMasse()));
        }
    }
}