package com.example.demo1wdassdfsdf;

import java.util.Random;

import javafx.collections.ObservableMap;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

abstract class MoveableBody extends Entity{

    public double Speed = 1.5; // self explanatory, the player's speed
    public double Smoothing = 80; // higher numbers mean more smoothing, but also slower circle

    MoveableBody(Group group, double initialSize){
        super(group, initialSize);
    }
    MoveableBody(double initialSize){
        super(initialSize);
    }
    
    public void checkCollision(){
        //go through each of the children of the root scene
        for(Node entity : AgarioApplication.root.getChildren()){
            if (entity instanceof Entity collider && !(entity instanceof Player)){

                //make sure we dont check if the body is colliding with itself
                if (entity != this){

                    //checks if the body is intersecting with the current child that we're looking at
                    Shape intersect = Shape.intersect(Sprite, collider.Sprite);

                    //if the body is colliding with something, increase the bodys size and remove the food from the scene
                    //this value will only be -1 if the player is colliding with nothing
                    if (intersect.getBoundsInLocal().getWidth() != -1){

                        double foodValue = 0.5;

                        //if the colliders sprite is smaller than this objects sprite, then eat the collider
                        if (isSmaller(collider.Sprite, Sprite)){
                            AgarioApplication.queueFree(collider);
                            foodValue += collider.Sprite.getRadius() / 20;
                            increaseSize(foodValue);
                        }
                    }
                }
            }

         }
    }

    private Boolean isSmaller(Circle circleOne, Circle circleTwo){
        if (circleOne.getRadius() > circleTwo.getRadius() + 2){
            return false;
        }
        return true;
    }


    public void increaseSize(double foodValue){
        //called whenever the player eats food
        //once the player gets big enough, we want the camera to start zooming out
        Sprite.setRadius(Sprite.getRadius() + foodValue);
        setViewOrder(-Sprite.getRadius());

    }
    
    public void moveToward(double[] velocity) {

        //initalize velocity, which is the mouse position - player position
        velocity = new double[]{velocity[0] - Sprite.getCenterX(), velocity[1] - Sprite.getCenterY()};

        //used for the smooth movement depending on how far away the mouse is.
        //further away from the circle, the faster the movement is etc.
        double magnitudeSmoothing = Math.sqrt( (velocity[0] * velocity[0]) + (velocity[1] * velocity[1])) / Smoothing;

        //limit speed of smoothing
        if (magnitudeSmoothing > 4){
            magnitudeSmoothing = 4 * Speed;
        }
        //normalize the position the player is going towards to get the direction
        velocity = normalizeDouble(velocity);

        //multiply direction by Speed to get the final velocity value, multiply smoothing for smoother movement
        velocity[0] *= Speed * magnitudeSmoothing;
        velocity[1] *= Speed * magnitudeSmoothing;

        //change Sprite position based on velocity
        //also check if the player is at the world limit, if it is then it doesnt move
        if (Sprite.getCenterX() + velocity[0] < AgarioApplication.getMapLimitWidth()){
            if (Sprite.getCenterX() + velocity[0] > -AgarioApplication.getMapLimitWidth()){
                Sprite.setCenterX(Sprite.getCenterX() + velocity[0] );
            }
        }
        if (Sprite.getCenterY() + velocity[1] < AgarioApplication.getMapLimitHeight()){
            if (Sprite.getCenterY() + velocity[1] > -AgarioApplication.getMapLimitHeight()){
                Sprite.setCenterY(Sprite.getCenterY() + velocity[1]);
            }
        }

    }



    public void splitSprite(){
        Player newBody = new Player(AgarioApplication.root, Sprite.getRadius() / 2);
        newBody.Sprite.setCenterX(Sprite.getCenterX() + 30);
        newBody.Sprite.setCenterY(Sprite.getCenterY() + 30);


        Sprite.setRadius(Sprite.getRadius() / 2);

    }
    public double distanceTo(double[] position){
        return Math.sqrt(Math.pow(position[0] - getPosition()[0], 2) + Math.pow(position[1] - getPosition()[1], 2) );
    }

    public double[] normalizeDouble(double[] array){
        //don't worry about it :)

        double magnitude = Math.sqrt( (array[0] * array[0]) + (array[1] * array[1]) );

        if (array[0] != 0 || array[1] != 0 ){
            return new double[]{array[0] / magnitude, array[1] / magnitude};
        }
        return new double[]{0,0};
    }


}
