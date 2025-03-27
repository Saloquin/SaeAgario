package com.example.sae.client.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Supplier;

import com.example.sae.client.ChatClient;
import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.Camera;
import com.example.sae.core.entity.*;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class SoloController implements Initializable {

    @FXML
    private StackPane rootStack;
    @FXML
    private Pane gameContainer;
    @FXML
    private AnchorPane hudContainer;
    @FXML
    private ListView<String> leaderboard;

    @FXML private Canvas minimap;
    private GraphicsContext minimapGC;
    private double minimapScale = 0.1; // Échelle de la minimap

    // Éléments du chat
    @FXML
    private VBox chatContainer;
    @FXML
    private ListView<String> chatListView;
    @FXML
    private TextField chatInput;

    @FXML private Label scoreLabel;
    @FXML private Label positionLabel;

    public static Group root;
    private static Solo client;
    private ChatClient chatClient;

    private Pane pane;
    private Player player;
    private Timeline updateTimeline;
    private Supplier<List<MoveableBody>> entitiesSupplier;

    private String playerName;
    private Color playerColor;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root = new Group();

        EntityFactory.setRoot(root);

        client = new Solo(root, playerName, playerColor);
        client.init();

        setupPlayerInfoUpdater();

        pane = client.createGamePane();

        pane.prefWidthProperty().bind(rootStack.widthProperty());
        pane.prefHeightProperty().bind(rootStack.heightProperty());

        gameContainer.getChildren().add(pane);
        player = client.getGameEngine().getPlayer(client.getPlayerId());

        // Initialisation du chat
        initializeChat();

        setCamera();
        initializeComponents();

        minimapGC = minimap.getGraphicsContext2D();
        setupMinimap();


        client.getGameIsEndedProperty().addListener((observable, oldValue, newValue) -> {
            stopGame();
            Stage stage = (Stage) gameContainer.getScene().getWindow();
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        rootStack.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> stopGame());
    }

    private void setupMinimap() {
        minimapGC.setFill(Color.LIGHTGRAY);
        minimapGC.fillRect(0, 0, minimap.getWidth(), minimap.getHeight());

        // Timer pour rafraîchir la minimap
        Timeline minimapUpdater = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    if(client.getGameEngine() != null) {
                        updateMinimap();
                    }
                }));
        minimapUpdater.setCycleCount(Timeline.INDEFINITE);
        minimapUpdater.play();
    }

    private void updateMinimap() {
        // Effacer la minimap
        minimapGC.setFill(Color.LIGHTGRAY);
        minimapGC.fillRect(0, 0, minimap.getWidth(), minimap.getHeight());

        // Dessiner les éléments
        drawWorldOnMinimap();
        drawPlayerOnMinimap();
        drawMinimapCenter();

    }

    private void setupPlayerInfoUpdater() {
        Timeline infoUpdater = new Timeline(
                new KeyFrame(Duration.millis(100), e -> updatePlayerInfo()));
        infoUpdater.setCycleCount(Timeline.INDEFINITE);
        infoUpdater.play();
    }

    private void updatePlayerInfo() {
        int currentScore = Integer.parseInt(scoreLabel.getText());
        int newScore = (int) Math.round(player.getMasse());

        if (newScore > currentScore) {
            scoreLabel.getStyleClass().add("score-increase");
            new Timeline(new KeyFrame(Duration.millis(500), e ->
                    scoreLabel.getStyleClass().remove("score-increase"))).play();
        }

        scoreLabel.setText(String.valueOf(newScore));

        int posX = (int) player.getX();
        int posY = (int) player.getY();
        positionLabel.setText(String.format("(%d, %d)", posX, posY));
    }

    private void drawMinimapCenter() {
        minimapGC.setStroke(Color.WHITE);
        minimapGC.setLineWidth(0.5);

        // Croix au centre
        double centerX = minimap.getWidth()/2;
        double centerY = minimap.getHeight()/2;
        double crossSize = 5;

        minimapGC.strokeLine(centerX - crossSize, centerY, centerX + crossSize, centerY);
        minimapGC.strokeLine(centerX, centerY - crossSize, centerX, centerY + crossSize);
    }

    private double calculateOptimalScale() {
        double worldWidth = 4000; // -2000 à 2000 = 4000 unités
        double worldHeight = 4000;

        double scaleX = minimap.getWidth() / worldWidth;
        double scaleY = minimap.getHeight() / worldHeight;
        System.out.println("scaleX: " + scaleX);
        System.out.println("scaleY: " + scaleY);


        return Math.min(scaleX, scaleY) * 0.9; // 10% de marge
    }

    private void drawWorldOnMinimap() {

        double scale = calculateOptimalScale();

        // Sauvegarde l'état actuel
        minimapGC.save();

        // Centre la vue sur (0,0) qui est le milieu de la map
        double translateX = minimap.getWidth()/2;
        double translateY = minimap.getHeight()/2;
        minimapGC.translate(translateX, translateY);

        // Dessin des entités
        for (MoveableBody entity : client.getGameEngine().getEntitiesMovable()) {
            double x = (entity.getX() * scale);
            double y = (entity.getY() * scale);


            double radius = entity.getSprite().getRadius();
            double scaleRadius = Math.max(3, radius * scale * 2);

            if (entity instanceof Player) {
                minimapGC.setFill(Color.BLUE);
            } else if (entity instanceof Enemy) {
                minimapGC.setFill(Color.RED);
            } else {
                minimapGC.setFill(Color.GREEN);
            }

            minimapGC.fillOval(x - scaleRadius/2, y - scaleRadius/2, scaleRadius, scaleRadius);
        }

        // Restaure l'état
        minimapGC.restore();
    }

    private void drawPlayerOnMinimap() {
        double scale = calculateOptimalScale();

        // Position du joueur centrée
        double playerX = minimap.getWidth()/2 + player.getX() * scale;
        double playerY = minimap.getHeight()/2 + player.getY() * scale;


        double radius = player.getSprite().getRadius();
        double scaleRadius = Math.max(3,radius * scale * 2);
        System.out.println("scaleRadius: " + scaleRadius);

        // Dessiner le joueur
        minimapGC.setFill(Color.YELLOW);
        minimapGC.fillOval(playerX - scaleRadius/2, playerY - scaleRadius/2, scaleRadius, scaleRadius);
    }

    private void initializeChat() {
        chatClient = new ChatClient(player.getNom(), message -> {
            javafx.application.Platform.runLater(() ->
                    chatListView.getItems().add(message)
            );
        });
        chatClient.start();

        chatInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                sendMessage();
            }
        });
    }

    @FXML
    public void sendMessage() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            chatClient.sendMessage(message);
            chatInput.clear();
        }
    }

    private void initializeComponents() {

        entitiesSupplier = () -> client.getGameEngine().getSortedMovableEntities();
        updateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateLeaderboard()));
        updateTimeline.setCycleCount(Timeline.INDEFINITE);
        updateTimeline.play();
    }

    private void updateLeaderboard() {
        List<MoveableBody> sortedEntities = entitiesSupplier.get();
        leaderboard.getItems().clear();
        for (MoveableBody entity : sortedEntities) {
            leaderboard.getItems().add(entity.getNom() + ": " + (int) Math.round(entity.getMasse()));
        }
    }

    public void stopGame() {
        if (client != null) {
            client.stopSoloGame();
        }
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
    }

    public static Client getClient() {
        return client;
    }

    private void setCamera() {
        Camera camera = client.getCamera();
        camera.focusPaneOn(pane, player);

    }
}