package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Player extends MoveableBody{


    private Camera camera;

    public Player(Group group, double masse, Color color){
        super(group, masse,color);
        setLayoutX(0);
        setLayoutY(0);
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
        AgarioApplication.queueFree(this);
    }

    @Override
    public void Update(){
        System.out.println("Camera Update"+ camera.getWorldOffset());
        System.out.println("Joueur position: " + getLayoutX() + ", " + getLayoutY());
        moveToward(AgarioApplication.getMousePosition());
        checkCollision();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera cam) {
        this.camera = cam;
    }
}
