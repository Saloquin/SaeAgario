package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;


public abstract class MoveableBody extends Entity{
    public double Speed = 1.5;
    public double Smoothing = 80; // higher numbers mean more smoothing, but also slower circle

    MoveableBody(Group group, double initialSize){
        super(group, initialSize);
    }
    MoveableBody(Group group, double initialSize, Color color){
        super(group, initialSize,color);
    }


    public void increaseSize(double foodValue){
        //called whenever the player eats food
        //once the player gets big enough, we want the camera to start zooming out*
        setMasse(getMasse() +foodValue);
        sprite.setRadius(10 * Math.sqrt(getMasse()));
        setViewOrder(-sprite.getRadius());

    }

    public void moveToward(double[] mousePosition) {
        double m = getMasse();
        double maxSpeed = 100 / Math.sqrt(m); // Ajuster 100 selon le besoin

        // Vecteur direction vers la souris
        double[] velocity = new double[]{
                mousePosition[0] - sprite.getCenterX(),
                mousePosition[1] - sprite.getCenterY()
        };

        // Distance du curseur au centre du joueur
        double distance = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]);

        // Normalisation du vecteur de direction
        if (distance > 0) {
            velocity[0] /= distance;
            velocity[1] /= distance;
        }

        // Facteur de vitesse (proportionnel à la distance du curseur au joueur, max à `maxSpeed`)
        double speedFactor = Math.min(distance / MAP_LIMIT_WIDTH, 1.0);
        double speed = maxSpeed * speedFactor;

        // Appliquer la vitesse calculée
        velocity[0] *= speed;
        velocity[1] *= speed;

        // Vérification des limites de la carte
        double newX = sprite.getCenterX() + velocity[0];
        double newY = sprite.getCenterY() + velocity[1];

        if (newX < MAP_LIMIT_WIDTH && newX > -MAP_LIMIT_WIDTH) {
            sprite.setCenterX(newX);
        }
        if (newY < MAP_LIMIT_HEIGHT && newY > -MAP_LIMIT_HEIGHT) {
            sprite.setCenterY(newY);
        }
    }

    //TODO: Implement the splitSprite method without using AgarioApplication.root
    public void splitSprite(){
        Player newBody = new Player(AgarioApplication.root, sprite.getRadius() / 2, Color.RED);
        newBody.sprite.setCenterX(sprite.getCenterX() + 30);
        newBody.sprite.setCenterY(sprite.getCenterY() + 30);


        sprite.setRadius(sprite.getRadius() / 2);

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
