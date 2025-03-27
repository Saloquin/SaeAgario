package com.example.sae.core.entity.player;

import com.example.sae.core.Camera;
import com.example.sae.core.entity.MoveableBody;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import com.example.sae.client.Solo;
import com.example.sae.client.Online;

import java.util.ArrayList;

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
    private boolean isLocal = false;

    private final DoubleProperty totalMasse = new SimpleDoubleProperty(0);

    /**
     * player's mouse position (direction):
     * local: send by mouse
     * online: send by server
     */
    protected double[] inputPosition;

    /**
     * ArrayList containing all the player's parts
     */
    protected ArrayList<PlayerPart> parts = new ArrayList<>();

    private void bindMasseProperty(){
        /*Bindings.createDoubleBinding(
                ()-> {
                    double sumMasse = 0;
                    for(PlayerPart p: parts){
                        sumMasse += p.getMasse();
                    }
                    return sumMasse;
                }
                ,totalMasse);*/
    }

    public void createFirstPart(Group group){
        parts.add(new PlayerPart(group, getMasse(), getColor()));
    }

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
        bindMasseProperty();
        createFirstPart(group);
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
        bindMasseProperty();
        createFirstPart(group);
    }



    public Player(Group group, double masse, Color color,String playerName){
        super(group, masse, color, playerName);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
        bindMasseProperty();
        createFirstPart(group);
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
        bindMasseProperty();
        createFirstPart(group);
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
        bindMasseProperty();
        createFirstPart(group);
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

    public void removePart(int index){
        parts.remove(index);
    }

    public void removePart(PlayerPart part){
        parts.remove(part);
    }

    public Player getPart(int index){
        return parts.get(index);
    }

    public void split(){
        System.out.println("BOMBARDILLO CROCODILO");
        System.out.println(parts.size());
        for(PlayerPart p : parts){
            p.split();
        }
    }

    public double getTotalMasse() {
        return totalMasse.get();
    }

    public DoubleProperty totalMasseProperty() {
        return totalMasse;
    }
}
