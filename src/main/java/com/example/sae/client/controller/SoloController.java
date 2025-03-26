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
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SoloController implements Initializable {

    @FXML
    private StackPane rootStack;
    @FXML
    private AnchorPane hudContainer;
    @FXML
    private ListView leaderboard;
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

        rootStack.getChildren().add(pane);

    }

    public void stopGame(){
        if(client != null) {
            client.stopSoloGame();
        }
    }

    public static Client getClient() {
        return client;
    }
}
