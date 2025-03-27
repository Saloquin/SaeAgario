package com.example.sae.client.controller;

import com.example.sae.client.controller.managers.*;
import com.example.sae.client.ChatClient;
import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.Camera;
import com.example.sae.core.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
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
    private static Pane pane;

    static public double[] getMousePosition(){
        java.awt.Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
        Point2D mousePos = root.screenToLocal(mouse.x, mouse.y);
        return new double[]{mousePos.getX(), mousePos.getY()};
    }

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
    }

    private void initializeGame() {
        root = new Group();

        EntityFactory.setRoot(root);

        client = new Solo(root, playerName, playerColor);
        client.init();

        pane = client.createGamePane();
        pane.prefWidthProperty().bind(rootStack.widthProperty());
        pane.prefHeightProperty().bind(rootStack.heightProperty());
        gameContainer.getChildren().add(pane);

        player = Client.getGameEngine().getPlayer(client.getPlayerId());


        client.getGameIsEndedProperty().addListener((observable, oldValue, newValue) -> {
            stopGame();
            Stage stage = (Stage) gameContainer.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        rootStack.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> stopGame());
    }

    private void initializeManagers() {
        minimapManager = new MinimapManager(minimap, player,
                () -> Client.getGameEngine().getEntitiesMovable());

        playerInfoManager = new PlayerInfoManager(player, scoreLabel, positionLabel);

        leaderboardManager = new LeaderboardManager(leaderboard,
                () -> Client.getGameEngine().getSortedMovableEntities());
    }

    private void initializeChat() {
        chatListView.setItems(FXCollections.observableArrayList());

        chatClient = new ChatClient(player.getNom(), message -> {
            javafx.application.Platform.runLater(() -> {
                ObservableList<String> messages = chatListView.getItems();
                messages.add(message);

                while (messages.size() > 6) {
                    messages.remove(0);
                }

                chatListView.scrollTo(messages.size() - 1);
            });
        });

        chatClient.start();

        chatInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
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
        if (minimapManager != null) minimapManager.stop();
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
    
    public static Pane getPane() {
        return pane;
    }
}