package com.example.sae.client.controller;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.client.controller.managers.LeaderboardManager;
import com.example.sae.client.controller.managers.MinimapManager;
import com.example.sae.client.controller.managers.PlayerInfoManager;
import com.example.sae.core.Camera;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.movable.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class GameController implements Initializable {
    private static Client client;
    private static Group root;
    private static Pane pane;
    @FXML
    private StackPane rootStack;
    @FXML
    private Pane gameContainer;
    @FXML
    private ListView<String> leaderboard;
    @FXML
    private Canvas minimap;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label positionLabel;
    private Player player;
    private MinimapManager minimapManager;
    private PlayerInfoManager playerInfoManager;
    private LeaderboardManager leaderboardManager;
    private String playerName;
    private Color playerColor;

    static public double[] getMousePosition() {
        java.awt.Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
        Point2D mousePos = root.screenToLocal(mouse.x, mouse.y);
        return new double[]{mousePos.getX(), mousePos.getY()};
    }

    public static Client getClient() {
        return client;
    }

    public static Group getRoot() {
        return root;
    }

    public static Pane getPane() {
        return pane;
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
                () -> Client.getGameEngine().getEntitiesMovable(), pane);

        playerInfoManager = new PlayerInfoManager(player, scoreLabel, positionLabel);

        leaderboardManager = new LeaderboardManager(leaderboard,
                () -> Client.getGameEngine().getSortedMovableEntities());

    }

    public void stopGame() {
        if (client != null) client.stopGame();
        if (leaderboardManager != null) leaderboardManager.stop();
        if (minimapManager != null) minimapManager.stop();
    }

    private void setCamera() {
        Camera camera = client.getCamera();
        camera.focusPaneOn(pane, player);
    }
}