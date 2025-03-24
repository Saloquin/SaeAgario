package com.example.sae.entity;

import com.example.sae.AgarioApplication;
import com.example.sae.Camera;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.ParallelCamera;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import static com.example.sae.AgarioApplication.*;

public class Player extends MoveableBody{


    private Camera camera;

    public Player(Group group, double masse, Color color){
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
        //camera.adjustZoom(this);

    }



    public void moveToward(double[] velocity) {
        super.moveToward(velocity);
    }



    public void gameOver(){
        AgarioApplication.queueFree(Sprite);
    }

    @Override
    public void Update(){
        moveToward(AgarioApplication.getMousePosition());
        checkCollision();
    }
    public ParallelCamera getCameraParallel() {
        return camera.getCamera();
    }
    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camou) {
        this.camera = camou;
    }
}
