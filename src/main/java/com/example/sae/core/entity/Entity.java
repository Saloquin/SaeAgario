package com.example.sae.core.entity;

import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import java.util.UUID;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public abstract class Entity extends Group{
    protected Circle sprite; // the entity's sprite
    private DoubleProperty masse;
    protected String entityId;

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

    Entity(Group group, double initialMasse, Color color) {
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
        if (group != null) {
            group.getChildren().add(this);
        }
    }

    Entity(Group group, String id, double initialMasse, Color color) {
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

    public double getMasse() {
        return masse.get();
    }

    public void setMasse(double value) {
        masse.set(value);
    }

    public DoubleProperty masseProperty() {
        return masse;
    }

    public void setPosition(double x, double y){
        sprite.setCenterX(x);
        sprite.setCenterY(y);
    }


    public double[] getPosition() {
        //returns current position of the sprite
        return new double[]{sprite.getCenterX(), sprite.getCenterY()};
    }

    public abstract void Update();

    public Circle getSprite() {
        return sprite;
    }

    public Color getColor() {
        return (Color) sprite.getFill();
    }

    public String getEntityId() {
        return this.entityId;
    }

    public void onDeletion() {
        // Remove from JavaFX scene graph if necessary
        if (getParent() != null) {
            ((Group) getParent()).getChildren().remove(this);
        }
    }

}
