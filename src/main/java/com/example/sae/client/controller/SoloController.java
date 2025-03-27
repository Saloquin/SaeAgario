package com.example.sae.client.controller;

import com.example.sae.client.controller.managers.*;
import com.example.sae.client.ChatClient;
import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.Camera;
import com.example.sae.core.entity.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class SoloController implements Initializable {
    @FXML private StackPane rootStack;
    @FXML private Pane gameContainer;
    @FXML private ListView<String> leaderboard;
    @FXML private Canvas minimap;
    @FXML private ListView<String> chatListView;
    @FXML private TextField chatInput;
    @FXML private Label scoreLabel;
    @FXML private Label positionLabel;

    private static Solo client;
    private static Group root;

    private Player player;
    private ChatClient chatClient;

    private MinimapManager minimapManager;
    private PlayerInfoManager playerInfoManager;
    private LeaderboardManager leaderboardManager;

    private String playerName;
    private Color playerColor;
    private Pane pane;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeGame();
        initializeManagers();
        initializeChat();
        setCamera();

        client.getGameIsEndedProperty().addListener((observable, oldValue, newValue) -> {
            stopGame();
            Stage stage = (Stage) gameContainer.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });
    }

    private void initializeGame() {
        root = new Group();
        client = new Solo(root, playerName, playerColor);
        client.init();

        pane = client.createGamePane(1280, 720);
        pane.prefWidthProperty().bind(rootStack.widthProperty());
        pane.prefHeightProperty().bind(rootStack.heightProperty());
        gameContainer.getChildren().add(pane);

        player = client.getGameEngine().getPlayer(client.getPlayerId());
    }

    private void initializeManagers() {

        minimapManager = new MinimapManager(minimap, player,
                () -> client.getGameEngine().getEntitiesMovable());

        playerInfoManager = new PlayerInfoManager(player, scoreLabel, positionLabel);

        leaderboardManager = new LeaderboardManager(leaderboard,
                () -> client.getGameEngine().getSortedMovableEntities());
    }

    private void initializeChat() {
        chatClient = new ChatClient(player.getNom(), message ->
                javafx.application.Platform.runLater(() ->
                        chatListView.getItems().add(message))
        );
        chatClient.start();

        // Ajoutez cet écouteur pour la touche Entrée
        chatInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                sendMessage();
            }
        });
    }

    @FXML
    private void sendMessage() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            chatClient.sendMessage(message);
            chatInput.clear();
        }
    }

    public void stopGame() {
        if (client != null) client.stopSoloGame();
        if (leaderboardManager != null) leaderboardManager.stop();
    }

    public static Client getClient() {
        return client;
    }

    public static Group getRoot() {
        return root;
    }

    private void setCamera() {
        Camera camera = client.getCamera();
        camera.focusPaneOn(pane, player);

    }
}