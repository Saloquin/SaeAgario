package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.entity.MoveableBody;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SoloController implements Initializable {

    @FXML
    public Pane gameContainer;
    @FXML
    private StackPane rootStack;
    @FXML
    private AnchorPane hudContainer;
    @FXML
    private ListView<String> leaderboard;
    @FXML
    private Canvas minimap;

    public static Group root;
    private static Solo client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        root = new Group();
        client = new Solo(root);
        client.init();

        Pane pane = client.createGamePane(1280, 720);

        pane.prefWidthProperty().bind(rootStack.widthProperty());
        pane.prefHeightProperty().bind(rootStack.heightProperty());

        gameContainer.getChildren().add(pane);

        startLeaderboardUpdateTimer();
    }

    public void stopGame(){
        if(client != null) {
            client.stopSoloGame();
        }
    }

    public void updateLeaderboard() {
        List<MoveableBody> sortedEntities = client.getGameEngine().getSortedMovableEntities();
        leaderboard.getItems().clear();
        for (MoveableBody entity : sortedEntities) {
            leaderboard.getItems().add(entity.getNom() + ": " + (int) Math.round(entity.getMasse()));
        }
    }

    private void startLeaderboardUpdateTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateLeaderboard()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static Client getClient() {
        return client;
    }
}