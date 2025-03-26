package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Player extends MoveableBody{



    private boolean isLocal = false; // Pour identifier si c'est un joueur local ou distant

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
    }

    @Override
    public void Update() {
        moveToward(AgarioApplication.getMousePosition());
    }

    public boolean isLocal() {
        return isLocal;
    }


    public void setCamera(Camera cam) {
        cam.focusOn(this);
    }

    @Override
    public void moveToward(double[] mousePosition) {
        super.moveToward(mousePosition);
    }
}
