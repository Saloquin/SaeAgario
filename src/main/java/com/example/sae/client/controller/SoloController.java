package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.entity.Player;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventType;
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
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.EventListener;
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

        client.getGameIsEndedProperty().addListener((observable, oldValue, newValue) -> {
            stopGame();
            Stage stage = (Stage) gameContainer.getScene().getWindow();
            stage.fireEvent(
                new WindowEvent(
                        stage, WindowEvent.WINDOW_CLOSE_REQUEST
                )
            );
        });

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
