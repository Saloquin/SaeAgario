package com.example.sae;


import com.example.sae.controller.MenuController;
import com.example.sae.entity.Enemy;
import com.example.sae.entity.Entity;
import com.example.sae.entity.Food;
import com.example.sae.entity.Player;
import com.example.sae.quadtree.Boundary;
import com.example.sae.quadtree.QuadTree;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AgarioApplication extends Application {
    public int maxTimer = 2;
    public int timer = maxTimer;
    public static int enemies = 0;
    private static QuadTree quadTree;
    private static final int QUAD_TREE_CAPACITY = 4;
    private static final int QUAD_TREE_MAX_DEPTH = 6;
    private static Scene scene;
    public static Group root = new Group();
    public static double mapLimitWidth = 2000;
    public static double mapLimitHeight = 2000;
    public static double ScreenWidth = 1280;
    public static double ScreenHeight = 720;
    public static ArrayList queuedObjectsForDeletion = new ArrayList<>();

    public static Player player;

    public static boolean gameStarted = false;
    private static AgarioApplication mainApp;

    @Override
    public void start(Stage stage) throws IOException {
        mainApp = this;
        GameTimer timer = new GameTimer(this);
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

    public static AgarioApplication getMainApp() {
        return mainApp;
    }

    public static void startGame(Stage stage) throws IOException {
        // Create and set root first
        root = new Group();
        scene = new Scene(root, ScreenWidth, ScreenHeight, Paint.valueOf("afafaf"));
        stage.setScene(scene);

        // Create camera
        Camera camera = new Camera();

        Boundary mapBoundary = new Boundary(0, 0, mapLimitWidth/2, mapLimitHeight/2);
        quadTree = new QuadTree(mapBoundary, QUAD_TREE_CAPACITY, QUAD_TREE_MAX_DEPTH);

        // Create player with camera
        player = new Player(root, 5, Color.RED);
        player.setCamera(camera);

        // Set camera focus
        scene.setCamera(camera.getCamera());
        camera.focusOn(player);

        // Add controls
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                player.splitSprite();
            }
        });

        // Create enemies
        Enemy enemy = new Enemy(root, 5);
        gameStarted = true;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        AgarioApplication.scene = scene;
    }

    public static Group getRoot() {
        return root;
    }

    public static void setRoot(Group root) {
        AgarioApplication.root = root;
    }

    public static void setMapLimitWidth(double mapLimitWidth) {
        AgarioApplication.mapLimitWidth = mapLimitWidth;
    }

    public static void setMapLimitHeight(double mapLimitHeight) {
        AgarioApplication.mapLimitHeight = mapLimitHeight;
    }

    public static void setScreenWidth(double screenWidth) {
        ScreenWidth = screenWidth;
    }

    public static void setScreenHeight(double screenHeight) {
        ScreenHeight = screenHeight;
    }

    public static ArrayList getQueuedObjectsForDeletion() {
        return queuedObjectsForDeletion;
    }

    public static void setQueuedObjectsForDeletion(ArrayList queuedObjectsForDeletion) {
        AgarioApplication.queuedObjectsForDeletion = queuedObjectsForDeletion;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        AgarioApplication.player = player;
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static void setGameStarted(boolean gameStarted) {
        AgarioApplication.gameStarted = gameStarted;
    }

    public int getMaxTimer() {
        return maxTimer;
    }

    public void setMaxTimer(int maxTimer) {
        this.maxTimer = maxTimer;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public static int getEnemies() {
        return enemies;
    }

    public static void setEnemies(int enemies) {
        AgarioApplication.enemies = enemies;
    }

    static public double getScreenWidth(){
        return scene.getWindow().getWidth();
    }
    static public double getScreenHeight(){
        return scene.getWindow().getHeight();
    }
    static public double getMapLimitWidth(){
        return mapLimitWidth;
    }
    static public double getMapLimitHeight(){
        return mapLimitHeight;
    }


    static public double[] getMousePosition(){
        java.awt.Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
        Point2D mousePos = root.screenToLocal(mouse.x, mouse.y);
        return new double[]{mousePos.getX(), mousePos.getY()};
    }


    public static void main(String[] args) {
        launch();
    }






    public void Update(){
        //does something every frame, put actions in here


        //spawn food every 5 frames
        if (timer <= 0){
            if (root.getChildren().size() < 1000){
                createFood();
            }
            
            timer = maxTimer; //reset the timer
        }

        if (enemies < 5){
            Enemy enemy = new Enemy(root, 5 );
            enemies++;
        }

        timer--; //decrement timer

    }



    public void createFood(){
        Food food = new Food(root, 2);
        quadTree.insert(food);
    }


    public static void queueFree(Object object){
        //there are errors when deleting objects inbetween of frames, mostly just unsafe in general
        //so when you want to delete an object, reference AgarioApplication and call this function queueFree
        //e.g. AgarioApplication.queueFree(foodSprite);
        //puts objects in a dynamic array, just means an array that doesnt have a fixed size
        //every frame before the update function is called, the objects in the queue will be deleted
        queuedObjectsForDeletion.add(object);
        Entity entity = (Entity) object;    
        entity.onDeletion();
    }

    public void freeQueuedObjects(){
        //deletes all objects in the queue
        //complicated to explain why we have to do it this way
        //just know if we dont, there will be tons of lag and errors every frame
        root.getChildren().removeAll(queuedObjectsForDeletion);
        queuedObjectsForDeletion.clear();

    }
    public static List<Entity> getNearbyEntities(Entity entity, double range) {
        Boundary searchArea = new Boundary(
                entity.Sprite.getCenterX(),
                entity.Sprite.getCenterY(),
                range,
                range
        );
        return quadTree.query(searchArea);
    }

}