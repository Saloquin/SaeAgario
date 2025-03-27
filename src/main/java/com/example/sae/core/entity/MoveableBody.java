package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

/**
 * moveable object, parent class of an AI or a player
 *
 * @see Entity
 * @see Enemy
 * @see Player
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public abstract class MoveableBody extends Entity{
    /**
     * name of moving object
     */
    public String name= "°-°";
    private Text nameText;
    private double actualSpeedX = 0;
    private double actualSpeedY = 0;
    public static final double BASE_MAX_SPEED = 15;
    public static final double ENEMY_SPEED_MULTIPLIER = 0.7;

    /**
     * constructor
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param initialSize size of moving object
     */
    MoveableBody(Group group, double initialSize) {
        super(group, initialSize);
        initializeNameText(group);
    }

    /**
     * constructor
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param initialSize size of moving object
     * @param color color of moving object
     */
    MoveableBody(Group group, double initialSize, Color color) {
        super(group, initialSize, color);
        initializeNameText(group);
    }
    /**
     * constructor
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param id entity id
     * @param initialSize size of moving object
     * @param color color of moving object
     */
    MoveableBody(Group group, String id, double initialSize, Color color) {
        super(group, id, initialSize, color);
        initializeNameText(group);
    }

    /**
     * constructor
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param initialSize size of moving object
     * @param color color of moving object
     * @param name name of moving object
     */
    MoveableBody(Group group, double initialSize, Color color, String name) {
        super(group, initialSize);
        this.name = name;
        sprite.setFill(color);
        initializeNameText(group);
    }

    /**
     * constructor
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param initialSize size of moving object
     * @param name name of moving object
     */
    MoveableBody(Group group, double initialSize, String name) {
        super(group, initialSize);
        this.name = name;
        initializeNameText(group);
    }

    /**
     * increases the size of the moving object that has eaten another moving object
     *
     * @see Entity
     * @see Player
     * @see Enemy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group the moving object that is eaten
     */
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

    /**
     * increases the size of the moving object that has eaten food
     *
     * @see Entity
     * @see Food
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param foodValue an entity, food eaten
     */
    public void increaseSize(double foodValue) {
        setMasse(getMasse() + foodValue);
        // Suppression de setViewOrder car déjà lié au rayon
        nameText.setX(sprite.getCenterX() - nameText.getLayoutBounds().getWidth() / 2);
        nameText.setY(sprite.getCenterY());
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




    /**
     * splits the moving object by clicking on the keyboard space bar
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    //TODO: Implement the splitSprite method without using AgarioApplication.root
    public void splitSprite(){
        Player newBody = EntityFactory.createPlayer(sprite.getRadius() / 2, Color.RED);
        newBody.sprite.setCenterX(sprite.getCenterX() + 30);
        newBody.sprite.setCenterY(sprite.getCenterY() + 30);


        sprite.setRadius(sprite.getRadius() / 2);

    }

    /**
     * calculates the distance between the moving object and another entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param position position of an entity as a double array
     * @return returns the distance between the position of the moving object and that of another entity
     */
    public double distanceTo(double[] position){
        return Math.sqrt(Math.pow(position[0] - getPosition()[0], 2) + Math.pow(position[1] - getPosition()[1], 2) );
    }

    /**
     * restructures an array of double
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param array table to be restructured
     * @return returns a restructured array of doubles
     */
    public double[] normalizeDouble(double[] array){
        //don't worry about it :)

        double magnitude = Math.sqrt( (array[0] * array[0]) + (array[1] * array[1]) );

        if (array[0] != 0 || array[1] != 0 ){
            return new double[]{array[0] / magnitude, array[1] / magnitude};
        }
        return new double[]{0,0};
    }

    /**
     * removes the moving object that is eaten
     *
     * @see Entity
     * @see Player
     * @see Enemy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    @Override
    public void onDeletion() {
        super.onDeletion();
        deleteText();
    }

    /**
     * removes the name text of moving object that is eaten
     *
     * @see Entity
     * @see Player
     * @see Enemy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    public void deleteText() {
        if (nameText.getParent() != null) {
            ((Group) nameText.getParent()).getChildren().remove(nameText);
        }
    }



}
