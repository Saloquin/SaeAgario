package com.example.sae.core.entity;

import com.example.sae.client.controller.SoloController;
import com.example.sae.client.utils.config.Constants;
import com.example.sae.core.Camera;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
public abstract class MoveableBody extends Entity {
    /**
     * name of moving object
     */

    private final StringProperty name = new SimpleStringProperty(this, "name", "°-°");
    private Text nameText;
    protected double actualSpeedX = 0;
    protected double actualSpeedY = 0;
    public static final double BASE_MAX_SPEED = Constants.getBaseMaxSpeed();
    public static final double ENEMY_SPEED_MULTIPLIER = Constants.getEnemySpeedMultiplier();
    protected double speedMultiplier = 1.0;


    public static final double DEFAULT_MASSE = 10; // Masse par défaut des joueurs

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
     * @param group       Group
     * @param initialSize size of moving object
     * @param color       color of moving object
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
     */
    MoveableBody(Group group, double initialSize, Color color) {
        super(group, initialSize, color);
        initializeNameText(group);
    }


    /**
     * constructor
     *
     * @param group       Group
     * @param id          entity id
     * @param initialSize size of moving object
     * @param color       color of moving object
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
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
     * @param id entity id
     * @param initialSize size of moving object
     * @param color color of moving object
     * @param playerName name of moving object
     */
    MoveableBody(Group group, String id, double initialSize, Color color, String playerName) {
        super(group, id, initialSize, color);
        this.name.set(playerName);
        initializeNameText(group);
    }

    /**
     * constructor
     *
     * @param group       Group
     * @param initialSize size of moving object
     * @param color       color of moving object
     * @param name        name of moving object
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
     */
    MoveableBody(Group group, double initialSize, Color color, String name) {
        super(group, initialSize, color);
        this.name.set(name);
        sprite.setFill(color);
        initializeNameText(group);
    }

    /**
     * constructor
     *
     * @param group       Group
     * @param initialSize size of moving object
     * @param name        name of moving object
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
     */
    MoveableBody(Group group, double initialSize, String name) {
        super(group, initialSize);
        this.name.set(name);
        initializeNameText(group);
    }

    /**
     * increases the size of the moving object that has eaten another moving object
     *
     * @param group the moving object that is eaten
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
     * @see Player
     * @see Enemy
     */
    private void initializeNameText(Group group) {
        nameText = new Text();
        nameText.textProperty().bind(name);
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
     * @param foodValue an entity, food eaten
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
     * @see Food
     */
    public void increaseSize(double foodValue) {
        setMasse(getMasse() + foodValue);
    }

    /**
     * moves the moving object according to the position of the mouse on the screen
     *
     * @param velocity the position of the mouse on the screen as a double array
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    public void moveToward(double[] velocity) {
        double maxSpeed = getMaxSpeed();

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
            actualSpeedX = 0;
            actualSpeedY = 0;
            return;
        }

        calculateSpeeds(distanceFromCenter);

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

    protected abstract void calculateSpeeds(double distanceFromCenter);

    public double getMaxSpeed() {
        return BASE_MAX_SPEED / (1 + Math.log10(getMasse()))*speedMultiplier;
    }


    /**
     * splits the moving object by clicking on the keyboard space bar
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    //TODO: Implement the splitSprite method without using AgarioApplication.root
    public void splitSprite() {
        Player newBody = EntityFactory.createPlayer(sprite.getRadius() / 2, (Color) sprite.getFill());
        newBody.sprite.setCenterX(sprite.getCenterX() + 30);
        newBody.sprite.setCenterY(sprite.getCenterY() + 30);


        sprite.setRadius(sprite.getRadius() / 2);

    }

    /**
     * calculates the distance between the moving object and another entity
     *
     * @param position position of an entity as a double array
     * @return returns the distance between the position of the moving object and that of another entity
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    public double distanceTo(double[] position) {
        return Math.sqrt(Math.pow(position[0] - getPosition()[0], 2) + Math.pow(position[1] - getPosition()[1], 2));
    }

    /**
     * restructures an array of double
     *
     * @param array table to be restructured
     * @return returns a restructured array of doubles
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */
    public double[] normalizeDouble(double[] array) {
        //don't worry about it :)

        double magnitude = Math.sqrt((array[0] * array[0]) + (array[1] * array[1]));

        if (array[0] != 0 || array[1] != 0) {
            return new double[]{array[0] / magnitude, array[1] / magnitude};
        }
        return new double[]{0, 0};
    }

    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }


    /**
     * removes the moving object that is eaten
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
     * @see Player
     * @see Enemy
     */
    @Override
    public void onDeletion() {
        super.onDeletion();
        deleteText();
    }

    /**
     * removes the name text of moving object that is eaten
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @see Entity
     * @see Player
     * @see Enemy
     */
    public void deleteText() {
        if (nameText.getParent() != null) {
            ((Group) nameText.getParent()).getChildren().remove(nameText);
        }
    }

    public void resetAllEffects() {
        setSpeedMultiplier(1.0);
    }

    public String getNom() {
        return name.get();
    }

    public void setNom(String nom) {
        this.name.set(nom);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getActualSpeedX() {
        return actualSpeedX;
    }

    public double getActualSpeedY() {
        return actualSpeedY;
    }
}