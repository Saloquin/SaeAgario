package com.example.sae;


import com.example.sae.controller.MenuController;
import com.example.sae.core.GameEngine;
import com.example.sae.entity.Enemy;
import com.example.sae.entity.Entity;
import com.example.sae.entity.Food;
import com.example.sae.entity.Player;
import com.example.sae.core.quadtree.Boundary;
import com.example.sae.core.quadtree.QuadTree;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;


public class AgarioApplication extends Application {

    private static Scene scene;
    public static final Group root = new Group();

    private static final double SCREEN_WIDTH = 1280;
    private static final double SCREEN_HEIGHT = 720;

    public static Player player;
    static boolean gameStarted = false;
    private static GameEngine gameEngine;
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
                gameEngine.update();
                updateGameLogic();
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        GameTimer timer = new GameTimer();
        timer.start();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/menu-view.fxml"));
        VBox root = loader.load();

        MenuController menuController = loader.getController();
        menuController.setStage(stage);

        scene = new Scene(root, 1280, 720);
        stage.setTitle("Agar.io");
        stage.setScene(scene);
        stage.show();
    }

    public static void startGame(Stage stage) throws IOException {
        gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
        gameStarted = true;
        scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, Paint.valueOf("afafaf"));


        // Configurer les événements souris immédiatement
        scene.setOnMouseMoved(AgarioApplication::handleMouseMove);
        scene.setOnMouseDragged(AgarioApplication::handleMouseMove);
        scene.setOnMousePressed(AgarioApplication::handleMouseMove);

        // Create camera
        Camera camera = new Camera();



        // Create player with camera
        player = new Player(root, 5, Color.RED);
        player.setCamera(camera);
        gameEngine.addEntity(player);

        // Set camera focus
        scene.setCamera(camera.getCamera());
        camera.focusOn(player);

        // Add controls
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                player.splitSprite();
            }
        });

        stage.setScene(scene);
    }

    private static void handleMouseMove(MouseEvent e) {
        Point2D localPoint = root.screenToLocal(e.getScreenX(), e.getScreenY());
        if (localPoint != null) {
            mousePosition = localPoint;
            mouseMoved = true;
        }
    }

    public static GameEngine getGameEngine() {
        return gameEngine;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        AgarioApplication.scene = scene;
    }


    public static double getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static double getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static double getMapLimitWidth() {
        return gameEngine.getWorldWidth();
    }

    public static double getMapLimitHeight() {
        return  gameEngine.getWorldHeight();
    }


    public static void main(String[] args) {
        launch();
    }


    private void updateGameLogic() {
        // Gestion de la création de nourriture
        if (gameEngine.getEntitiesOfType(Food.class).size() < 100) {
            Food food = new Food(root, 2);
            gameEngine.addEntity(food);
        }

        // Gestion des ennemis - Augmenter le nombre minimum d'ennemis
        int currentEnemies = gameEngine.getEntitiesOfType(Enemy.class).size();
        if (currentEnemies < 10) { // Augmenter de 5 à 10 ennemis
            Enemy enemy = new Enemy(root, 5);
            gameEngine.addEntity(enemy);
        }
    }

    public static double[] getMousePosition() {
        // Si la souris n'a pas encore bougé, retourner le centre de l'écran
        if (!mouseMoved) {
            return new double[]{SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2};
        }
        return new double[]{mousePosition.getX(), mousePosition.getY()};
    }


    public static void queueFree(Entity entity) {
        // Remove from GameEngine instead of using a queue
        if (gameEngine != null) {
            gameEngine.removeEntity(entity);
            entity.onDeletion();

            // If the entity is in the JavaFX scene graph, remove it
            if (entity.getParent() != null) {
                root.getChildren().remove(entity);
            }
        }
    }




}