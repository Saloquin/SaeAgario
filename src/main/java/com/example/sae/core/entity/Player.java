package com.example.sae.core.entity;

import com.example.sae.core.Camera;
import com.example.sae.core.entity.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.core.entity.enemyStrategy.EnemyStrategy;
import com.example.sae.core.entity.enemyStrategy.RandomMoveStrategy;
import com.example.sae.core.entity.enemyStrategy.SeekFoodStrategy;
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
     * camera that follows the player's moving object during a game
     */
    private Camera camera;

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
        super(group, masse,color);
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
     * increases the size of the player's moving object that has eaten food (depending on zoom)
     *
     * @see Entity
     * @see Food
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param foodValue an entity, food eaten
     */
    public void increaseSize(double foodValue){
        super.increaseSize(foodValue);
        //camera.adjustZoom(this);

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
     * returns the player's moving object camera
     *
     * @see MoveableBody
     * @see Camera
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns the player's moving object camera
     */
    public Camera getCamera() {
        return camera;
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
        this.camera = cam;
    }
}
