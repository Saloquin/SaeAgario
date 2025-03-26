package com.example.sae.core.entity;

import com.example.sae.core.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import com.example.sae.client.Solo;
import com.example.sae.client.Online;

/**
 * moving object used by the player during a game
 *
 * @see MoveableBody
 * @see Camera
 * @see Online
 * @see Solo
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
     * player's mouse position (direction):
     * local: send by mouse
     * online: send by server
     */
    private double[] inputPosition; // Position cible (souris pour le joueur local, position reçue du serveur pour les autres)

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
        inputPosition = new double[]{0, 0};
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
        inputPosition = new double[] { x, y };
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
        moveToward(inputPosition);
        if (isLocal && camera != null) {
            camera.focusOn(this);
        }
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
     * changes the direction of the player's moving object :
     * local: sent by mouse
     * online: sent by server
     *
     * @see MoveableBody
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param position new direction in the form of an array of doubles
     */
    public void setInputPosition(double[] position) {
        this.inputPosition = position;
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
}
