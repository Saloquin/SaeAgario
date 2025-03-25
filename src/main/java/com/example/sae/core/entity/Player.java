package com.example.sae.core.entity;

import com.example.sae.core.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Player extends MoveableBody{


    private Camera camera;
    private boolean isLocal = false; // Pour identifier si c'est un joueur local ou distant
    private double[] inputPosition; // Position cible (souris pour le joueur local, position reçue du serveur pour les autres)

    public Player(Group group, double masse, Color color){
        super(group, masse,color);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());

    }

    public Player(Group group, double masse, Color color, boolean isLocal) {
        super(group, masse, color);
        this.isLocal = isLocal;
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
        inputPosition = new double[]{0, 0};
    }

    public void increaseSize(double foodValue){
        super.increaseSize(foodValue);
        //camera.adjustZoom(this);

    }

    public void moveToward(double[] velocity) {
        super.moveToward(velocity);
        camera.focusOn(this);
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setInputPosition(double[] position) {
        this.inputPosition = position;
    }

    @Override
    public void Update() {
        moveToward(inputPosition);
    }
    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera cam) {
        this.camera = cam;
    }
}
