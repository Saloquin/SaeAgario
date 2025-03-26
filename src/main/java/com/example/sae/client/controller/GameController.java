package com.example.sae.client.controller;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Player;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;


public class GameController {
    @FXML
    private Canvas minimap;

    @FXML
    private ListView<String> leaderboard;

    @FXML
    private ListView<String> listChat;

    @FXML
    private TextField sendMessage;

    @FXML
    public AnchorPane hudContainer;

    @FXML
    public Pane gameContainer;

    private ObservableList<Player> leaderboardItems = FXCollections.observableArrayList();

    public Pane getGameContainer() {
        return gameContainer;
    }

    private final double WORLD_WIDTH = 1280;
    private final double WORLD_HEIGHT = 720;

    @FXML
    public void initialize() {
        // Boucle d'animation : juste pour redessiner la minimap régulièrement
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawMinimap();
            }
        }.start();
    }

    

    private void drawMinimap() {
        GraphicsContext gc = minimap.getGraphicsContext2D();
        gc.clearRect(0, 0, minimap.getWidth(), minimap.getHeight());

        double scaleX = 0.15;
        double scaleY = 0.15;


        // Fond
        gc.setFill(Color.rgb(240, 240, 240, 0.9));
        gc.fillRect(0, 0, minimap.getWidth(), minimap.getHeight());

        SnapshotParameters params = new SnapshotParameters();
        params.setTransform(new Scale(scaleX, scaleY));
        WritableImage image = new WritableImage((int)(WORLD_WIDTH*scaleX), (int)(WORLD_HEIGHT*scaleY));

        gameContainer.getChildren().add(new Circle(89));
        gameContainer.snapshot(params,image);





        gc.drawImage(image,0,0);


    }


}
