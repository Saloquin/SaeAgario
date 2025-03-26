package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
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

import java.net.URL;
import java.util.ResourceBundle;

public class OnlineController  implements Initializable {

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
    private ListView chat;
    @FXML
    private TextField message;

    private static Client client;

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
            if(client instanceof Solo) {
                ((Solo) client).stopSoloGame();
            }
        }
    }

    public static Client getClient() {
        return client;
    }
}
