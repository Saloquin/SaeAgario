package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SoloController implements Initializable {

    @FXML
    private StackPane rootStack;
    @FXML
    private Pane gameContainer;
    @FXML
    private AnchorPane hudContainer;
    @FXML
    private ListView leaderboard;
    @FXML
    private Canvas minimap;

    private static Solo client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Group root = new Group();
        client = new Solo(root);
        client.init();
        Scene scene = client.createGameScene(1280,720);
        gameContainer.getChildren().add(scene.getRoot());
    }

    public void stopGame(){
        if(client != null) {
            client.stopSoloGame();
        }
        Platform.exit();
    }

    public static Client getClient() {
        return client;
    }
}
