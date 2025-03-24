package com.example.sae;

import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Player extends MoveableBody{


    public ParallelCamera camera = new ParallelCamera(); // creates a camera for the player
    
    public double[] cameraScale = {camera.getScaleX(), camera.getScaleY()};

    Player(Group group, double masse, Color color){
        super(group, masse,color);
        Sprite.setCenterX(0);
        Sprite.setCenterY(0);
        Sprite.setViewOrder(-Sprite.getRadius());

    }
    Player(double initialSize){
        super(initialSize);
        //new player made and added to the group
        Sprite.setCenterX(0);
        Sprite.setCenterY(0);

        //puts the player infront of all the food
        Sprite.setViewOrder(-Sprite.getRadius());

    }


    public void increaseSize(double foodValue){
        super.increaseSize(foodValue);
        //zoom out the camera when the player gets too big

        ScaleTransition cameraZoom = new ScaleTransition(Duration.millis(200), camera);

        if (Sprite.getRadius() > 70){
            cameraScale[0] += foodValue / 200;
            cameraScale[1] += foodValue / 200;
        }

        cameraZoom.setToX(cameraScale[0]);
        cameraZoom.setToY(cameraScale[1]);
        cameraZoom.play();

    }

    public void moveToward(double[] velocity) {
        super.moveToward(velocity);
        velocity = normalizeDouble(velocity);
        //set the position of the camera to the same position as the player
        //minus by half of the screen resolution, keep the player in the middle of the screen
        camera.setLayoutX((Sprite.getCenterX() + velocity[0]) - AgarioApplication.getScreenWidth() / 2 * camera.getScaleX());
        camera.setLayoutY((Sprite.getCenterY() + velocity[1])  - AgarioApplication.getScreenHeight() / 2 * camera.getScaleY());
    }


    public void gameOver(){
        AgarioApplication.queueFree(Sprite);
    }

    @Override
    public void Update(){
        //move player towards the mouse position
        moveToward(AgarioApplication.getMousePosition());

        //check if player is colliding with anything
        checkCollision();



    }

}
