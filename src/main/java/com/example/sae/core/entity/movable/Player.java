package com.example.sae.core.entity.movable;

import com.example.sae.client.Client;
import com.example.sae.client.controller.SoloController;
import com.example.sae.core.Camera;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.movable.body.MoveableBody;
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
        super(group, masse, color,playerName);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }

    @Override
    public void splitSprite() {
        Player clone = EntityFactory.createPlayer(getMasse()/2,getNom(), (Color) sprite.getFill());
        clone.sprite.setCenterX(sprite.getCenterX() + CLONE_SPLIT_DISTANCE);
        clone.sprite.setCenterY(sprite.getCenterY() + CLONE_SPLIT_DISTANCE);
        setMasse(getMasse() / 2);
        clone.setComposite(this.composite);
        Client.getGameEngine().addEntity(clone);
        addClone(clone);
        this.composite.updateLastSplitTime();
    }

    @Override
    protected void calculateSpeeds(double distanceFromCenter) {
        double currentScale = sprite.getParent().getScaleX();
        double maxDistanceH = (sprite.getParent().getScene().getHeight()/2) / currentScale;
        double maxDistanceW = (sprite.getParent().getScene().getWidth()/2) / currentScale;

        double speedFactorX = Math.min(distanceFromCenter / maxDistanceW, 1.0);
        double speedFactorY = Math.min(distanceFromCenter / maxDistanceH, 1.0);

        actualSpeedX = getMaxSpeed() * speedFactorX ;
        actualSpeedY = getMaxSpeed() * speedFactorY ;
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

    public DoubleProperty getCenterXProperty() {
        return sprite.centerXProperty();
    }

    public DoubleProperty getCenterYProperty() {
        return sprite.centerYProperty();
    }
}
