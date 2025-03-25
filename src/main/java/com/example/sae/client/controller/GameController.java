package com.example.sae.client.controller;

import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Player;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameController {
/*
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
    private ListView chatList;
    @FXML
    private TextField chatInput;

    private GameEngine engine;
    private Player player;
    private static Group root = new Group();
    private static boolean gameStarted = false;
    private static Point2D mousePosition = new Point2D(0, 0);
    private static boolean mouseMoved = false;

    private class GameTimer extends AnimationTimer {
        private final double framesPerSecond = 120;
        private final double interval = 1000000000 / framesPerSecond;
        private double last = 0;

        @Override
        public void handle(long now) {
            if (last == 0) {
                last = now;
            }

            if (now - last > interval && gameStarted) {
                last = now;
                engine.update();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startGame(Stage stage, boolean isOnline) {
        engine = new GameEngine(GameEngine.MAP_LIMIT_WIDTH, GameEngine.MAP_LIMIT_HEIGHT, isOnline);
        gameStarted = true;

        // Créer caméra
        Camera camera = new Camera();
        //camera.attachTo(root);

        // Créer joueur
        player = new Player(root, 5, Color.RED);
        player.setCamera(camera);
        engine.addEntity(player);

        // Ajouter le joueur au gameContainer
        gameContainer.getChildren().add(player);

        // Maintenant on peut dire à la caméra de suivre le joueur
        camera.focusOn(player);

        // Événements clavier/souris
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                player.splitSprite();
            }
        });

        GameTimer timer = new GameTimer();
        timer.start();

        gameContainer.getChildren().add(root);

        stage.getScene().setOnMouseMoved(this::handleMouseMove);
        stage.getScene().setOnMouseDragged(this::handleMouseMove);
        stage.getScene().setOnMousePressed(this::handleMouseMove);


        stage.getScene().setCamera(camera.getCamera());


        // Vérifiez la position et la visibilité du joueur
        System.out.println("Player Position: " + Arrays.toString(player.getPosition()));
        System.out.println("Player Visible: " + player.isVisible());
    }

    private void handleMouseMove(MouseEvent e) {
        Point2D localPoint = root.screenToLocal(e.getScreenX(), e.getScreenY());
        if (localPoint != null) {
            mousePosition = localPoint;
            mouseMoved = true;
        }
    }

    public void getPlayerPosition() {
        System.out.println(Arrays.toString(player.getPosition()));
        System.out.println(engine.getEntities().size());
    }

 */
}
