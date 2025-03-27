package com.example.sae.core.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;
import java.util.UUID;

/**
 * An abstract class for physical representation of an entity on a plan
 *
 * @see EntityFactory
 * @see MoveableBody
 * @see Food
 */
public abstract class Entity extends Group {

    /// the entity's sprite
    protected Circle sprite;
    /// the entity's id
    protected String entityId;
    /// the entity's mass
    private final DoubleProperty mass;

    /**
     * @param group       the group on which the entity is displayed
     * @param initialMass the entity's initial mass
     */
    Entity(Group group, double initialMass) {
        super();
        this.entityId = UUID.randomUUID().toString();
        this.mass = new SimpleDoubleProperty(initialMass);
        Random rand = new Random();

        setSprite(group, Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), 0.99));
    }

    /**
     * @param group       the group on which the entity is displayed
     * @param initialMass the entity's initial mass
     * @param color       the entity's color
     */
    protected Entity(Group group, double initialMass, Color color) {
        super();
        this.entityId = UUID.randomUUID().toString();
        this.mass = new SimpleDoubleProperty(initialMass);

        setSprite(group, color);
    }

    /**
     * @param group       the group on which the entity is displayed
     * @param id          the entity's id
     * @param initialMass the entity's initial mass
     * @param color       the entity's color
     */
    Entity(Group group, String id, double initialMass, Color color) {
        super();
        this.entityId = id;
        this.mass = new SimpleDoubleProperty(initialMass);

        setSprite(group, color);
    }

    /**
     * set the sprite of the entity on the group plan
     *
     * @param group the plan on which the entity is created
     * @param color the color of the entity's sprite
     */
    private void setSprite(Group group, Color color) {
        sprite = new Circle();
        sprite.setFill(color);

        // Bind the sprite radius to the entity's mass
        sprite.radiusProperty().bind(mass.multiply(10)
                .map(number -> Math.sqrt(number.doubleValue())));

        // Bind the viewOrder to the sprite's radius
        viewOrderProperty().bind(sprite.radiusProperty().negate());

        getChildren().add(sprite);
        if (group != null) {
            group.getChildren().add(this);
        }
    }

    /**
     * {@return the double value of the entity's mass property}
     */
    public double getMasse() {
        return mass.get();
    }

    /**
     * change the entity's mass property's value
     *
     * @param value the new mass
     */
    public void setMasse(double value) {
        mass.set(value);
    }

    /**
     * {@return the current coordinates of the center of the entity's sprite}
     */
    public double[] getPosition() {
        return new double[]{sprite.getCenterX(), sprite.getCenterY()};
    }

    /**
     * update the entity on the plan
     */
    public abstract void Update();

    /**
     * @see Circle
     * {@return the circle representing the sprite}
     */
    public Circle getSprite() {
        return sprite;
    }

    /**
     * {@return the X coordinate of the center of the sprite}
     */
    public double getX() {
        return sprite.getCenterX();
    }

    public double getY() {
        return sprite.getCenterY();
    }


    /**
     * @see Color
     * {@return the entity's sprite's color}
     */
    public Color getColor() {
        return (Color) sprite.getFill();
    }

    /**
     * {@return the entity's id}
     */
    public String getEntityId() {
        return this.entityId;
    }

    /**
     * remove the entity from the existence plan
     */
    public void onDeletion() {
        // Remove from JavaFX scene graph if necessary
        if (getParent() != null) {
            ((Group) getParent()).getChildren().remove(this);
        }
    }

}
