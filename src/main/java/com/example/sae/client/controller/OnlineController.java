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
import javafx.scene.paint.Color;

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
        client = new Solo(root, "Player", Color.BEIGE);
        client.init();
        Pane scene = client.createGamePane();
        gameContainer.getChildren().add(scene);
    }

    public static Client getClient() {
        return client;
    }
}
