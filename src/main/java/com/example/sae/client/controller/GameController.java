package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.GameEngine;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController {

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
    @FXML
    private ListView chatList;
    @FXML
    private TextField chatInput;

    private static final Group root = new Group();

    private static Client client;

    public void stopGame(){
        if(client != null) {
            if(client instanceof Solo) {
                ((Solo) client).stopSoloGame();
            }
        }
    }

    public void startSoloGame() {
        client = new Solo(root);
        client.init();
        Scene scene = client.createGameScene(GameEngine.MAP_LIMIT_WIDTH,GameEngine.MAP_LIMIT_HEIGHT);
        gameContainer.getChildren().add(scene.getRoot());
    }

    public static Client getClient() {
        return client;
    }
}
