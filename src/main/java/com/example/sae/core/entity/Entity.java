package com.example.sae.core.entity;

import java.util.Random;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public abstract class Entity extends Group{
    public Circle Sprite; // the entity's sprite
    private double masse;

    Entity(Group group, double masse){
        super();
        this.masse = masse;
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);

        Sprite = new Circle(10*Math.sqrt(masse), Color.rgb(r, g , b, 0.99));
        
        setViewOrder(-Sprite.getRadius());
        Sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(Sprite);
        group.getChildren().add(this);
    }
    Entity(Group group,double masse, Color color){
        super();
        this.masse = masse;
        Random rand = new Random();

        Sprite = new Circle(10*Math.sqrt(masse), color);

        setViewOrder(-Sprite.getRadius());
        Sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(Sprite);
        group.getChildren().add(this);
    }

    Entity(double masse){
        super();
        this.masse = masse;
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);

        Sprite = new Circle(10*Math.sqrt(masse), Color.rgb(r, g , b, 0.99));

        setViewOrder(-Sprite.getRadius());
        Sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(Sprite);
    }

    public double getMasse() {
        return masse;
    }

    public void setMasse(double masse) {
        this.masse = masse;
    }

    public double[] getPosition() {
        //returns current position of the sprite
        return new double[]{Sprite.getCenterX(), Sprite.getCenterY()};
    }

    public void Update(){

    }

    public void onDeletion() {
        // Remove from JavaFX scene graph if necessary
        if (getParent() != null) {
            ((Group) getParent()).getChildren().remove(this);
        }
    }

}
