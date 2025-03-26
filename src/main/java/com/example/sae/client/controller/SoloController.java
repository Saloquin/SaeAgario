package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.Camera;
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

    private Pane pane;

    private Player player;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        root = new Group();
        client = new Solo(root);
        client.init();

        pane = client.createGamePane(1280, 720);
        pane.prefWidthProperty().bind(rootStack.widthProperty());
        pane.prefHeightProperty().bind(rootStack.heightProperty());

        gameContainer.getChildren().add(pane);
        player = client.getGameEngine().getPlayer(client.getPlayerId());

        setCamera();

        client.getGameIsEndedProperty().addListener((observable, oldValue, newValue) -> {
            stopGame();
            Stage stage = (Stage) gameContainer.getScene().getWindow();
            stage.fireEvent(
                new WindowEvent(
                        stage, WindowEvent.WINDOW_CLOSE_REQUEST
                )
            );
        });

        rootStack.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            stopGame();
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

    private void setCamera(){
        Camera camera = client.getCamera();
        camera.focusPaneOn(pane, player);
        // Ajouter des listeners pour afficher les positions du joueur
        player.getCenterXProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Player X: " + newValue);
        });

        player.getCenterYProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Player Y: " + newValue);
        });
    }
}
