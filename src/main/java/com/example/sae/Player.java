package com.example.sae;

import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.example.sae.AgarioApplication.*;

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

        // Centrer la caméra sur le joueur
        camera.setLayoutX(Sprite.getCenterX() - (getScreenWidth() / 2) * camera.getScaleX());
        camera.setLayoutY(Sprite.getCenterY() - (AgarioApplication.getScreenHeight() / 2) * camera.getScaleY());
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
