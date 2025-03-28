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
 * player in the game
 *
 * @see MoveableBody
 */
public class Player extends MoveableBody {

    /**
     * indicate if the player is created on an online or offline game
     *
     * @see Online
     * @see Solo
     */
    private boolean isLocal = false;


    /**
     * @param group the group on which the player is moving
     * @param mass the player's initial size
     * @param color the player's color
     * @see MoveableBody
     */
    public Player(Group group, double mass, Color color) {
        super(group, mass, color);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }

    /**
     * @param group the group on which the player is moving
     * @param id the player's id
     * @param mass the player's initial size
     * @param color the player's color
     * @see MoveableBody
     */
    public Player(Group group, String id, double mass, Color color) {
        super(group, id, mass, color);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }

    /**
     * @param group the group on which the player is moving
     * @param mass the player's initial size
     * @param color the player's color
     * @param playerName the player's name
     * @see MoveableBody
     */
    public Player(Group group, double mass, Color color, String playerName) {
        super(group, mass, color, playerName);
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }

    /**
     * @param group the group on which the player is moving
     * @param mass the player's initial size
     * @param color the player's color
     * @param isLocal if the player is on a local game
     * @see MoveableBody
     */
    public Player(Group group, double mass, Color color, boolean isLocal) {
        super(group, mass, color);
        this.isLocal = isLocal;
        sprite.setCenterX(0);
        sprite.setCenterY(0);
        sprite.setViewOrder(-sprite.getRadius());
    }


    /**
     * @param group the group on which the player is moving
     * @param id the player's id
     * @param x the player's initial x coordinate
     * @param y the player's initial y coordinate
     * @param mass the player's initial size
     * @param color the player's color
     * @param isLocal if the player is on a local game
     * @see MoveableBody
     */
    public Player(Group group, String id, double x, double y, double mass, Color color, boolean isLocal) {
        super(group, id, mass, color);
        this.isLocal = isLocal;
        sprite.setCenterX(x);
        sprite.setCenterY(y);
        sprite.setViewOrder(-sprite.getRadius());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void calculateSpeeds(double distanceFromCenter) {
        double currentScale = SoloController.getPane().getScaleX();
        double maxDistanceH = (SoloController.getPane().getScene().getHeight() / 2) / currentScale;
        double maxDistanceW = (SoloController.getPane().getScene().getWidth() / 2) / currentScale;

        double speedFactorX = Math.min(distanceFromCenter / maxDistanceW, 1.0);
        double speedFactorY = Math.min(distanceFromCenter / maxDistanceH, 1.0);

        actualSpeedX = getMaxSpeed() * speedFactorX;
        actualSpeedY = getMaxSpeed() * speedFactorY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        moveToward(SoloController.getMousePosition());
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

}
