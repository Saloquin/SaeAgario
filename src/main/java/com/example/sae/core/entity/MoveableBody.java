package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.GameEngine;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


public abstract class MoveableBody extends Entity{
    public double Speed = 1.5;
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

    public void checkCollision() {
        // Obtenir le GameEngine via AgarioApplicationTest2
        GameEngine gameEngine = AgarioApplication.getGameEngine();
        if (gameEngine == null) return;

        // Utiliser la liste thread-safe du GameEngine
        for (Entity collider : gameEngine.getEntities()) {
            if (collider != this) {
                Shape intersect = Shape.intersect(Sprite, collider.Sprite);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    if (isOverlappingEnough(this, collider, 0.33)) {
                        if (isSmaller(collider.Sprite, Sprite)) {
                            AgarioApplication.queueFree(collider);
                            increaseSize(collider.getMasse());
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

        // Direction vers la souris
        double dx = mousePosition[0] - getLayoutX();
        double dy = mousePosition[1] - getLayoutY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Normalisation
        if (distance == 0) return;

        dx /= distance;
        dy /= distance;

        // Calcul de la vitesse
        double speedFactor = Math.min(distance / AgarioApplication.getScreenWidth(), 1.0);
        double speed = maxSpeed * speedFactor;

        double newX = getLayoutX() + dx * speed;
        double newY = getLayoutY() + dy * speed;

        // Limites de la carte
        newX = Math.max(0, Math.min(newX, AgarioApplication.getMapLimitWidth()));
        newY = Math.max(0, Math.min(newY, AgarioApplication.getMapLimitHeight()));

        // Appliquer déplacement
        setLayoutX(newX);
        setLayoutY(newY);
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
