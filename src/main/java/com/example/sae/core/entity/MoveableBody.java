package com.example.sae.core.entity;

import com.example.sae.client.AgarioApplication;
import com.example.sae.core.entity.enemyStrategy.ChaseClosestEntityStrategy;
import com.example.sae.core.entity.enemyStrategy.EnemyStrategy;
import com.example.sae.core.entity.enemyStrategy.RandomMoveStrategy;
import com.example.sae.core.entity.enemyStrategy.SeekFoodStrategy;
import javafx.beans.binding.Bindings;
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
     * speed of moving object
     */
    public double Speed = 1.5;
    /**
     * name of moving object
     */
    public String name= "°-°";

    private Text nameText;

    public static final double BASE_MAX_SPEED = 45.0; // Vitesse de base maximum

    /**
     *  minimum entity speed
     */
    public static final double MIN_MAX_SPEED = 7.0;  // Vitesse maximum minimale

    public static final double SPEED_FACTOR = 1.5;

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
     * @param initialSize size of moving object
     * @param color color of moving object
     * @param name name of moving object
     */
    MoveableBody(Group group, double initialSize,Color color, String name) {
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
    MoveableBody(Group group, double initialSize,String name) {
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

        // Position initiale au centre du cercle
        nameText.setX(sprite.getCenterX() - nameText.getLayoutBounds().getWidth() / 2);
        nameText.setY(sprite.getCenterY());

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
        sprite.setRadius(10 * Math.sqrt(getMasse()));
        setViewOrder(-sprite.getRadius());
        nameText.setX(sprite.getCenterX() - nameText.getLayoutBounds().getWidth() / 2);
        nameText.setY(sprite.getCenterY());

    }

    /**
     * moves the moving object according to the position of the mouse on the screen
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param mousePosition the position of the mouse on the screen as a double array
     */
    public void moveToward(double[] mousePosition) {
        double m = getMasse();
        // La vitesse maximum ne peut pas descendre en dessous de MIN_MAX_SPEED
        double maxSpeed = Math.max(BASE_MAX_SPEED / Math.sqrt(m), MIN_MAX_SPEED);

        // Vecteur direction vers la souris
        double[] velocity = new double[]{
                mousePosition[0] - sprite.getCenterX(),
                mousePosition[1] - sprite.getCenterY()
        };

        // Distance du curseur au centre du joueur
        double distance = Math.sqrt(velocity[0] * velocity[0] + velocity[1] * velocity[1]);

        // Si la souris est au même endroit que le joueur, pas de mouvement
        if (distance < 1) {
            return;
        }

        // Normalisation du vecteur de direction
        velocity[0] /= distance;
        velocity[1] /= distance;

        // Le facteur de vitesse est proportionnel à la distance
        // Distance maximale considérée pour la vitesse (rayon d'influence)
        double maxDistance = 200.0;
        double speedFactor = Math.min(distance / maxDistance, 1.0);
        double currentSpeed = maxSpeed * speedFactor * SPEED_FACTOR;

        // Application de la vitesse
        velocity[0] *= currentSpeed;
        velocity[1] *= currentSpeed;

        // Vérification des limites de la carte
        double newX = sprite.getCenterX() + velocity[0];
        double newY = sprite.getCenterY() + velocity[1];

        if (newX < MAP_LIMIT_WIDTH && newX > -MAP_LIMIT_WIDTH) {
            sprite.setCenterX(newX);
            nameText.setX(newX - nameText.getLayoutBounds().getWidth() / 2);
        }
        if (newY < MAP_LIMIT_HEIGHT && newY > -MAP_LIMIT_HEIGHT) {
            sprite.setCenterY(newY);
            nameText.setY(newY);
        }
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
        if (nameText.getParent() != null) {
            ((Group) nameText.getParent()).getChildren().remove(nameText);
        }
    }


}
