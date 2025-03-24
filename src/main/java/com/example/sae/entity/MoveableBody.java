package com.example.sae.entity;

import com.example.sae.AgarioApplication;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


public abstract class MoveableBody extends Entity{
    public double Speed = 1.5; // self explanatory, the player's speed
    public double Smoothing = 80; // higher numbers mean more smoothing, but also slower circle

    MoveableBody(Group group, double initialSize){
        super(group, initialSize);
    }
    MoveableBody(Group group, double initialSize, Color color){
        super(group, initialSize,color);
    }
    MoveableBody(double initialSize){
        super(initialSize);
    }
    
    public void checkCollision(){
        //go through each of the children of the root scene
        for(Node entity : AgarioApplication.root.getChildren()){
            if (entity instanceof Entity collider /*&& !(entity instanceof Player)*/){

                //make sure we dont check if the body is colliding with itself
                if (entity != this){

                    //checks if the body is intersecting with the current child that we're looking at
                    Shape intersect = Shape.intersect(Sprite, collider.Sprite);

                    //if the body is colliding with something, increase the bodys size and remove the food from the scene
                    //this value will only be -1 if the player is colliding with nothing
                    if (intersect.getBoundsInLocal().getWidth() != -1){
                        if (isOverlappingEnough(this, entity, 0.33)) {
                            if (isSmaller(collider.Sprite, Sprite)) {
                                AgarioApplication.queueFree(collider);
                                increaseSize(((Entity) entity).getMasse());
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isOverlappingEnough(Node a, Node b, double threshold) {
        Shape intersect = Shape.intersect(((Entity) a).Sprite, ((Entity) b).Sprite);

        double intersectionArea = intersect.getLayoutBounds().getWidth() * intersect.getLayoutBounds().getHeight();
        double areaA = ((Entity) a).Sprite.getLayoutBounds().getWidth() * ((Entity) a).Sprite.getLayoutBounds().getHeight();

        return (intersectionArea / areaA) <= threshold;
    }


    private Boolean isSmaller(Circle circleOne, Circle circleTwo){
        if ((Math.pow(circleOne.getRadius(),2)/100)*1.33 > (Math.pow(circleTwo.getRadius(),2)/100)){
            return false;
        }
        return true;
    }


    public void increaseSize(double foodValue){
        //called whenever the player eats food
        //once the player gets big enough, we want the camera to start zooming out*
        setMasse(getMasse() +foodValue);
        Sprite.setRadius(10 * Math.sqrt(getMasse()));
        setViewOrder(-Sprite.getRadius());

    }

    public void moveToward(double[] mousePosition) {
        double m = getMasse();
        double maxSpeed = 100 / Math.sqrt(m); // Ajuster 100 selon le besoin

        // Vecteur direction vers la souris
        double[] velocity = new double[]{
                mousePosition[0] - Sprite.getCenterX(),
                mousePosition[1] - Sprite.getCenterY()
        };

        // Distance du curseur au centre du joueur
        double distance = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]);

        // Normalisation du vecteur de direction
        if (distance > 0) {
            velocity[0] /= distance;
            velocity[1] /= distance;
        }

        // Facteur de vitesse (proportionnel à la distance du curseur au joueur, max à `maxSpeed`)
        double speedFactor = Math.min(distance / AgarioApplication.getScreenWidth(), 1.0);
        double speed = maxSpeed * speedFactor;

        // Appliquer la vitesse calculée
        velocity[0] *= speed;
        velocity[1] *= speed;

        // Vérification des limites de la carte
        double newX = Sprite.getCenterX() + velocity[0];
        double newY = Sprite.getCenterY() + velocity[1];

        if (newX < AgarioApplication.getMapLimitWidth() && newX > -AgarioApplication.getMapLimitWidth()) {
            Sprite.setCenterX(newX);
        }
        if (newY < AgarioApplication.getMapLimitHeight() && newY > -AgarioApplication.getMapLimitHeight()) {
            Sprite.setCenterY(newY);
        }
    }




    public void splitSprite(){
        Player newBody = new Player(AgarioApplication.root, Sprite.getRadius() / 2, Color.RED);
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
