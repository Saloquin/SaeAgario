package com.example.sae.core.entity;

import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.util.UUID;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * entity: a physical body representing food, an AI/enemy or a player
 *
 * @see MoveableBody
 * @see Player
 * @see Enemy
 * @see Food
 * @see Circle
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public abstract class Entity extends Group{
    /**
     * entity's physical body
     */
    protected Circle sprite; // the entity's sprite
    /**
     * entity mass: used to determine its size and speed
     */
    private DoubleProperty masse;

    protected String entityId;

    /**
     * constructor
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param initialMasse size of entity
     */
    Entity(Group group, double initialMasse) {
        super();
        this.entityId = UUID.randomUUID().toString();
        this.masse = new SimpleDoubleProperty(initialMasse);
        Random rand = new Random();

        sprite = new Circle();
        sprite.setFill(Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), 0.99));

        // Bind le rayon à la masse avec conversion explicite
        sprite.radiusProperty().bind(masse.multiply(10)
                .map(number -> Math.sqrt(number.doubleValue())));

        // Bind le viewOrder au rayon
        viewOrderProperty().bind(sprite.radiusProperty().negate());

        getChildren().add(sprite);
        if (group != null) {
            group.getChildren().add(this);
        }
    }

    /**
     * constructor
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param initialMasse size of entity
     * @param color color of entity
     */
    protected Entity(Group group, double initialMasse, Color color) {
        super();

        this.entityId = UUID.randomUUID().toString();
        this.masse = new SimpleDoubleProperty(initialMasse);

        sprite = new Circle();
        sprite.setFill(color);

        // Bind le rayon à la masse avec conversion explicite
        sprite.radiusProperty().bind(masse.multiply(10)
                .map(number -> Math.sqrt(number.doubleValue())));

        // Bind le viewOrder au rayon
        viewOrderProperty().bind(sprite.radiusProperty().negate());
        getChildren().add(sprite);
        group.getChildren().add(this);
    }

    /**
     * constructor
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param id id of entity
     * @param initialMasse size of entity
     * @param color color of entity
     */
    protected Entity(Group group, String id, double initialMasse, Color color) {
        super();
        this.entityId = id;
        this.masse = new SimpleDoubleProperty(initialMasse);

        sprite = new Circle();
        sprite.setFill(color);

        // Bind le rayon à la masse avec conversion explicite
        sprite.radiusProperty().bind(masse.multiply(10)
                .map(number -> Math.sqrt(number.doubleValue())));

        // Bind le viewOrder au rayon
        viewOrderProperty().bind(sprite.radiusProperty().negate());

        getChildren().add(sprite);
        if (group != null) {
            group.getChildren().add(this);
        }
    }

    /**
     * returns the entity's mass
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns the entity's mass
     */
    public double getMasse() {
        return masse.get();
    }

    /**
     * changes the entity's mass
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param value new entity's mass
     */
    public void setMasse(double value) {
        masse.set(value);
    }

    public DoubleProperty masseProperty() {
        return masse;
    }

    /**
     * changes the entity's position
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param x new entity coordinates
     * @param y new entity coordinates
     */
    public void setPosition(double x, double y){
        sprite.setCenterX(x);
        sprite.setCenterY(y);
    }

    /**
     * returns the entity's position
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns the entity's position
     */
    public double[] getPosition() {
        //returns current position of the sprite
        return new double[]{sprite.getCenterX(), sprite.getCenterY()};
    }

    public abstract void Update();

    /**
     * returns the entity's physical body
     *
     * @see Circle
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns the entity's position
     */
    public Circle getSprite() {
        return sprite;
    }

    public double getX(){
        return sprite.getCenterX();
    }

    public double getY(){
        return sprite.getCenterY();
    }



    /**
     * returns the entity's color
     *
     * @see Color
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns the entity's color
     */
    public Color getColor() {
        return (Color) sprite.getFill();
    }

    /**
     * returns entity id
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return returns entity id
     */
    public String getEntityId() {
        return this.entityId;
    }

    /**
     * changes the entity id
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param id the new entity id
     */
    public void setEntityId(String id) {
        this.entityId = id;
    }

    /**
     * removes the entity that is eaten
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    public void onDeletion() {
        // Remove from JavaFX scene graph if necessary
        if (getParent() != null) {
            ((Group) getParent()).getChildren().remove(this);
        }
    }

}
