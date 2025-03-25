package com.example.sae.client;


import com.example.sae.client.controller.GameController;
import com.example.sae.client.controller.MenuController;
import com.example.sae.core.Camera;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Enemy;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.scene.Parent;

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
    /*
    @Override
    public void start(Stage stage) throws IOException {
        GameTimer timer = new GameTimer();
        timer.start();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/menu.fxml"));
        VBox root = loader.load();

        MenuController menuController = loader.getController();
        menuController.setStage(stage);

        scene = new Scene(root, 1280, 720);
        stage.setTitle("Agar.io");
        stage.setScene(scene);
        stage.show();
    }

     */

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Lancer le timer
        GameTimer timer = new GameTimer();
        timer.start();
        // Charger l'interface depuis le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/menu.fxml"));
        Parent root = loader.load();

        // Charge le controlleur
        MenuController controller = loader.getController();
        controller.setStage(primaryStage);

        // Créer la scène et l'afficher
        Scene scene = new Scene(root ,1280,720);
        primaryStage.setTitle("Agario Menu");
        primaryStage.setScene(scene);

        primaryStage.show();




    }

    public static void main(String[] args) {
        launch(args);
    }
    public static void startGame(Stage stage, Pane gameContainer) {
        gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
        gameStarted = true;

        // Ajouter le monde de jeu à la partie mobile
        gameContainer.getChildren().add(root);

        // Créer caméra
        Camera camera = new Camera();
        camera.attachTo(root);

        // Créer joueur
        player = new Player(root, 5, Color.RED);
        player.setCamera(camera);
        gameEngine.addEntity(player);

        // Maintenant on peut dire à la caméra de suivre le joueur
        camera.focusOn(player);

        // Événements clavier/souris
        scene = stage.getScene(); // ← Assure-toi que la scène existe bien ici
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                player.splitSprite();
            }
        });

        scene.setOnMouseMoved(AgarioApplication::handleMouseMove);
        scene.setOnMouseDragged(AgarioApplication::handleMouseMove);
        scene.setOnMousePressed(AgarioApplication::handleMouseMove);
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

    /*
    public static void main(String[] args) {
        launch();
    }

     */


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