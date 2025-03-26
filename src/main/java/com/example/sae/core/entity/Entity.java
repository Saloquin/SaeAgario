package com.example.sae.core.entity;

import java.util.Random;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public abstract class Entity extends Group{
    protected Circle sprite; // the entity's sprite
    private double masse;

    Entity(Group group, double masse){
        super();
        this.masse = masse;
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);

        sprite = new Circle(10*Math.sqrt(masse), Color.rgb(r, g , b, 0.99));
        
        setViewOrder(-sprite.getRadius());
        sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(sprite);
        group.getChildren().add(this);
    }
    Entity(Group group,double masse, Color color){
        super();
        this.masse = masse;
        Random rand = new Random();

        sprite = new Circle(10*Math.sqrt(masse), color);

        setViewOrder(-sprite.getRadius());
        sprite.setRadius(10*Math.sqrt(masse));
        getChildren().add(sprite);
        group.getChildren().add(this);
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


    public void onDeletion() {
        // Remove from JavaFX scene graph if necessary
        if (getParent() != null) {
            ((Group) getParent()).getChildren().remove(this);
        }
    }

}
