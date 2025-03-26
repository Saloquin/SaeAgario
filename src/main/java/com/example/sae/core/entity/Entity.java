package com.example.sae.core.entity;

import java.util.Random;
import java.util.UUID;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public abstract class Entity extends Group{
    protected Circle sprite; // the entity's sprite
    private double masse;
    protected String entityId;

    Entity(Group group,double masse){
        super();
        this.entityId = UUID.randomUUID().toString();
        this.masse = masse;
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);

        sprite = new Circle(10*Math.sqrt(masse), Color.rgb(r, g , b, 0.99));
        
        setViewOrder(-sprite.getRadius());
        sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(sprite);
        if (group != null) {
            group.getChildren().add(this);
        }
    }
    Entity(Group group,double masse, Color color){
        super();
        this.entityId = UUID.randomUUID().toString();
        this.masse = masse;

        sprite = new Circle(10*Math.sqrt(masse), color);

        setViewOrder(-sprite.getRadius());
        sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(sprite);
        if (group != null) {
            group.getChildren().add(this);
        }
    }
    Entity(Group group, String id, double masse, Color color){
        super();
        this.entityId = id;
        this.masse = masse;

        sprite = new Circle(10*Math.sqrt(masse), color);

        setViewOrder(-sprite.getRadius());
        sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(sprite);
        if (group != null) {
            group.getChildren().add(this);
        }
    }

    public void setRandomColor(){
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);
        sprite.setFill(Color.rgb(r, g , b, 0.99));
    }

    Entity(double masse){
        super();
        this.entityId = UUID.randomUUID().toString();
        this.masse = masse;
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);

        sprite = new Circle(10*Math.sqrt(masse), Color.rgb(r, g , b, 0.99));

        setViewOrder(-sprite.getRadius());
        sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(sprite);
    }

    public void setPosition(double x, double y){
        sprite.setCenterX(x);
        sprite.setCenterY(y);
    }

    public double getMasse() {
        return masse;
    }

    public void setMasse(double masse) {
        this.masse = masse;
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
