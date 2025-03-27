package com.example.sae.client.controller.managers;

import com.example.sae.core.entity.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 */
public class PlayerInfoManager {
    private final Player player;
    private final Label scoreLabel;
    private final Label positionLabel;

    public PlayerInfoManager(Player player, Label scoreLabel, Label positionLabel) {
        this.player = player;
        this.scoreLabel = scoreLabel;
        this.positionLabel = positionLabel;
        initialize();
    }

    private void initialize() {
        Timeline updater = new Timeline(
                new KeyFrame(Duration.millis(100), e -> update()));
        updater.setCycleCount(Timeline.INDEFINITE);
        updater.play();
    }

    private void update() {
        updateScore();
        updatePosition();
    }

    private void updateScore() {
        int currentScore = Integer.parseInt(scoreLabel.getText());
        int newScore = (int) Math.round(player.getMasse());

        if (newScore > currentScore) {
            scoreLabel.getStyleClass().add("score-increase");
            new Timeline(new KeyFrame(Duration.millis(500),
                    e -> scoreLabel.getStyleClass().remove("score-increase"))).play();
        }

        scoreLabel.setText(String.valueOf(newScore));
    }

    private void updatePosition() {
        positionLabel.setText(String.format("(%d, %d)",
                (int) player.getX(), (int) player.getY()));
    }
}