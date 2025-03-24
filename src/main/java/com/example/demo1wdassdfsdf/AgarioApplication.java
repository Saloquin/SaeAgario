package com.example.demo1wdassdfsdf;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.EventHandler;
import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;


public class           AgarioApplication extends Application {


    public class GameTimer extends AnimationTimer{

        private double framesPerSecond = 120;
        private double interval = 1000000000 / framesPerSecond;
        private double last = 0;
        @Override
        public void handle(long now) {

            if (last == 0){
                last = now;
            }

            if (now - last > interval ){
                if (gameStarted){
                    last = now;
                    freeQueuedObjects(); // deletes any objects queued up to be free
                    for (Node entity : root.getChildren()){
                        Entity convertedEntity = (Entity) entity;
                        convertedEntity.Update();
                    }
                    Update(); //calls update function every frame
                }

            }
        }
    }

    private static Scene scene;
    public static Group root = new Group();
    private static double mapLimitWidth = 2000;
    private static double mapLimitHeight = 2000;
    private static double ScreenWidth = 1280;
    private static double ScreenHeight = 720;
    private static ArrayList queuedObjectsForDeletion = new ArrayList<>();

    public static Player player;

    public static boolean gameStarted = false;


    @Override
    public void start(Stage stage) throws IOException {
        //create a player object, add it to the group, the initial radius 50

        //start the gametimer
        GameTimer timer = new GameTimer();
        timer.start();

        Menu menu = new Menu(stage);
        scene = new Scene(menu, ScreenWidth, ScreenHeight, Paint.valueOf("afafaf"));
        stage.setTitle("Agar.io");
        stage.setScene(scene);
        stage.show();


    }

    public static void startGame(Stage stage){

        player = new Player(root, 50);
        Enemy enemy = new Enemy(root, 50);

        scene = new Scene(root, ScreenWidth, ScreenHeight, Paint.valueOf("afafaf"));
        //set the camera to the players camera, which will follow the player
        scene.setCamera(player.camera);

        scene.setOnKeyPressed(e ->{
            if (e.getCode() == KeyCode.SPACE){
                player.splitSprite();
            }
        });
        stage.setScene(scene);


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


    public int maxTimer = 2;
    public int timer = maxTimer;
    public static int enemies = 0;

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
            Enemy enemy = new Enemy(root, 50);
            enemies++;
        }

        timer--; //decrement timer

    }



    public void createFood(){
        Food food = new Food(root, 10);
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

    private void freeQueuedObjects(){
        //deletes all objects in the queue
        //complicated to explain why we have to do it this way
        //just know if we dont, there will be tons of lag and errors every frame
        root.getChildren().removeAll(queuedObjectsForDeletion);
        queuedObjectsForDeletion.clear();

    }
}