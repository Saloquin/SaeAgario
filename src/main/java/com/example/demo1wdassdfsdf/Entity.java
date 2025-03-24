package com.example.demo1wdassdfsdf;

import java.util.Random;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


abstract class Entity extends Group{
    public Circle Sprite; // the entity's sprite

    Entity(Group group, double initialSize){
        super();

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);

        Sprite = new Circle(initialSize, Color.rgb(r, g , b, 0.99));
        
        setViewOrder(-Sprite.getRadius());
        Sprite.setRadius(initialSize);
        getChildren().add(Sprite);
        group.getChildren().add(this);
    }

    Entity(double initialSize){
        super();

        Random rand = new Random();
        int r = rand.nextInt(255);
        int g  = rand.nextInt(255);
        int b  = rand.nextInt(255);

        Sprite = new Circle(initialSize, Color.rgb(r, g , b, 0.99));

        setViewOrder(-Sprite.getRadius());
        Sprite.setRadius(initialSize);
        getChildren().add(Sprite);
    }
    public double[] getPosition() {
        //returns current position of the sprite
        return new double[]{Sprite.getCenterX(), Sprite.getCenterY()};
    }

    public void Update(){

    }

    public void onDeletion(){

    }

}
