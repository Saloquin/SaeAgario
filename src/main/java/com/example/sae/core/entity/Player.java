package com.example.sae.core.entity;

import com.example.sae.core.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Player extends MoveableBody{



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

    @Override
    public void Update() {
        moveToward(inputPosition);
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setInputPosition(double[] position) {
        this.inputPosition = position;
    }

    public void setCamera(Camera cam) {
        cam.focusOn(this);
    }
}
