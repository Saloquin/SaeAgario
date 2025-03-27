package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.client.controller.SoloController;
import com.example.sae.client.factory.GamePaneFactory;
import com.example.sae.core.Camera;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import com.example.sae.client.Solo;
import com.example.sae.client.Online;

/**
 * moving object used by the player during a game
 *
 * @see MoveableBody
 * @see Camera
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class Player extends MoveableBody{



    /**
     * Boolean that determines whether the player's mobile object is in the local or online game
     *
     * @see Online
     * @see Solo
     */
    private boolean isLocal = false; // Pour identifier si c'est un joueur local ou distant

    /**
     * constructor
     *
     * @see MoveableBody
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param masse mass of the player's moving object
     * @param color name of the player's moving object
     */
    public Player(Group group, double masse, Color color){
        super(group, masse, color);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }

    /**
     * constructor
     *
     * @see MoveableBody
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param id entity id
     * @param masse mass of the player's moving object
     * @param color name of the player's moving object
     */
    public Player(Group group, String id, double masse, Color color){
        super(group, id, masse, color);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }



    public Player(Group group, double masse, Color color,String playerName){
        super(group, masse, color, playerName);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }


    /**
     * constructor
     *
     * @see MoveableBody
     * @see Online
     * @see Solo
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param masse mass of the player's moving object
     * @param color name of the player's moving object
     * @param isLocal Boolean that determines whether the player's mobile object is in the local or online game
     */
    public Player(Group group, double masse, Color color, boolean isLocal) {
        super(group, masse, color);
        this.isLocal = isLocal;
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }

    /**
     * constructor
     *
     * @see MoveableBody
     * @see Online
     * @see Solo
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param id entity id
     * @param x coordinate of moving object
     * @param y coordinate of moving object
     * @param masse mass of the player's moving object
     * @param color name of the player's moving object
     * @param isLocal Boolean that determines whether the player's mobile object is in the local or online game
     */
    public Player(Group group, String id, double x, double y, double masse, Color color, boolean isLocal) {
        super(group, id, masse, color);
        this.isLocal = isLocal;
        sprite.setCenterX(x);
        sprite.setCenterY(y);
        sprite.setViewOrder(-sprite.getRadius());
    }

    /**
     *
     *
     * @see MoveableBody
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    @Override
    public void Update() {
        moveToward(SoloController.getMousePosition());
    }

    /**
     * returns whether the player's mobile object is in the local or online game
     *
     * @see MoveableBody
     * @see Online
     * @see Solo
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns true if the player's mobile object is in the local game
     */
    public boolean isLocal() {
        return isLocal;
    }


    /**
     * changes the camera of the player's moving object
     *
     * @see MoveableBody
     * @see Camera
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param cam the new camera
     */
    public void setCamera(Camera cam) {
        cam.focusOn(this);
    }

    @Override
    public void moveToward(double[] mousePosition) {
        super.moveToward(mousePosition);
    }

    public DoubleProperty getCenterXProperty() {
        return sprite.centerXProperty();
    }

    public DoubleProperty getCenterYProperty() {
        return sprite.centerYProperty();
    }
}
