package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.client.controller.components.ChatController;
import com.example.sae.client.controller.components.LeaderboardController;
import com.example.sae.client.controller.components.MinimapController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SoloController implements Initializable {
    @FXML
    public Pane gameContainer;
    @FXML
    private StackPane rootStack;
    @FXML
    private AnchorPane hudContainer;

    @FXML
    private ChatController chatController;
    @FXML
    private LeaderboardController leaderboardController;
    @FXML
    private AnchorPane minimap;


    public static Group root;
    private static Solo client;

    private MinimapController minimapController;

    private String playerName;
    private Color playerColor;

    public void setPlayerColor(Color color) {
        this.playerColor = color;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root = new Group();
        client = new Solo(root, playerName, playerColor);
        client.init();

        Pane pane = client.createGamePane(1280, 720);
        pane.prefWidthProperty().bind(rootStack.widthProperty());
        pane.prefHeightProperty().bind(rootStack.heightProperty());
        gameContainer.getChildren().add(pane);

        initializeComponents();
    }

    private void initializeComponents() {
        initMinimap();
        chatController.initialize(playerName);
        leaderboardController.initializeWithSupplier(() -> client.getGameEngine().getSortedMovableEntities());
    }

    public void initMinimap() {
        try {
            System.out.println("Chargement minimap.fxml depuis : " + getClass().getResource("/com/example/sae/minimap.fxml"));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/minimap.fxml"));
            AnchorPane minimapNode = loader.load();
            minimapController = loader.getController();

            System.out.println("MinimapController chargé : " + minimapController);

            minimapController.setupMinimap();



            minimap.getChildren().add(minimapNode);
            AnchorPane.setBottomAnchor(minimapNode, 10.0);
            AnchorPane.setRightAnchor(minimapNode, 10.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopGame() {
        if (client != null) {
            minimapController.stop();
            client.stopSoloGame();
            leaderboardController.stop();
        }
    }

    public static Client getClient() {
        return client;
    }
}