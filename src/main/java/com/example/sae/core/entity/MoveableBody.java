package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.client.debug.DebugWindowController;
import com.example.sae.core.GameEngine;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;


public abstract class MoveableBody extends Entity{
    public String name= "°-°";
    private Text nameText;
    private double actualSpeedX = 0;
    private double actualSpeedY = 0;
    public static final double BASE_MAX_SPEED = 15;
    public static final double ENEMY_SPEED_MULTIPLIER = 0.7;


    MoveableBody(Group group, double initialSize) {
        super(group, initialSize);
        initializeNameText(group);
    }

    MoveableBody(Group group, double initialSize, Color color) {
        super(group, initialSize, color);
        initializeNameText(group);

    }
    MoveableBody(Group group, double initialSize,Color color, String name) {
        super(group, initialSize);
        this.name = name;
        sprite.setFill(color);
        initializeNameText(group);
    }
    MoveableBody(Group group, double initialSize,String name) {
        super(group, initialSize);
        this.name = name;
        initializeNameText(group);
    }

    public double getActualSpeedX() {
        return actualSpeedX;
    }
    public double getActualSpeedY() {
        return actualSpeedY;
    }

    private void initializeNameText(Group group) {
        nameText = new Text(name);
        nameText.setFill(Color.BLACK);
        nameText.setStyle("-fx-font-size: 14;");
        // Place le texte au-dessus du sprite dans l'ordre de rendu
        nameText.setViewOrder(-1000);

        nameText.styleProperty().bind(Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1f;", sprite.getRadius()),
                sprite.radiusProperty()
        ));

        nameText.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            nameText.setX(sprite.getCenterX() - newBounds.getWidth() / 2);
        });

        // Binding simple pour Y
        nameText.yProperty().bind(sprite.centerYProperty());

        // Binding pour X avec le centre du sprite
        sprite.centerXProperty().addListener((obs, oldX, newX) -> {
            nameText.setX(newX.doubleValue() - nameText.getLayoutBounds().getWidth() / 2);
        });
        group.getChildren().add(nameText);
    }


    public void increaseSize(double foodValue) {
        setMasse(getMasse() + foodValue);
    }

    public void moveToward(double[] velocity) {
        double maxSpeed = getMaxSpeed();

        if (this instanceof Enemy) {
            maxSpeed *= ENEMY_SPEED_MULTIPLIER;
        }

        // Vecteur de direction (souris - position du joueur)
        double[] direction = new double[]{
                velocity[0] - sprite.getCenterX(),
                velocity[1] - sprite.getCenterY()
        };

        // Distance du curseur par rapport au centre
        double distanceFromCenter = Math.sqrt(
                direction[0] * direction[0] +
                        direction[1] * direction[1]
        );

        // Normalisation du vecteur de direction
        double[] normalizedDirection = normalizeDouble(direction);

        // Zone morte au centre (10 pixels)
        if (distanceFromCenter <= 10) {
            actualSpeedY = 0;
            actualSpeedX = 0;
            return;
        }


        double maxDistanceH = 200;
        double maxDistanceW = 300;
        double speedFactorX = Math.min(distanceFromCenter / (maxDistanceW), 1.0);
        double speedFactorY = Math.min(distanceFromCenter / (maxDistanceH), 1.0);

        DebugWindowController.addLog(distanceFromCenter + " " + maxDistanceW );


        // Application de la vitesse
        actualSpeedX = maxSpeed * speedFactorX;
        actualSpeedY = maxSpeed * speedFactorY;
        double dx = normalizedDirection[0] * actualSpeedX;
        double dy = normalizedDirection[1] * actualSpeedY;

        // Mise à jour de la position avec vérification des limites
        if (sprite.getCenterX() + dx < MAP_LIMIT_WIDTH && sprite.getCenterX() + dx > -MAP_LIMIT_WIDTH) {
            sprite.setCenterX(sprite.getCenterX() + dx);
        }
        if (sprite.getCenterY() + dy < MAP_LIMIT_HEIGHT && sprite.getCenterY() + dy > -MAP_LIMIT_HEIGHT) {
            sprite.setCenterY(sprite.getCenterY() + dy);
        }


    }

    public double getMaxSpeed() {
        return BASE_MAX_SPEED / (1+Math.log10(getMasse()));
    }




    //TODO: Implement the splitSprite method without using AgarioApplication.root
    public void splitSprite(){
        Player newBody = EntityFactory.createPlayer(sprite.getRadius() / 2, Color.RED);
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

    @Override
    public void onDeletion() {
        super.onDeletion();
        deleteText();
    }

    public void deleteText() {
        if (nameText.getParent() != null) {
            ((Group) nameText.getParent()).getChildren().remove(nameText);
        }
    }



}
