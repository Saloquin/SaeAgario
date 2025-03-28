package com.example.sae.core.entity.movable.body;

import com.example.sae.client.utils.config.Constants;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.immobile.Food;
import com.example.sae.core.entity.movable.Enemy;
import com.example.sae.core.entity.movable.Player;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

/**
 * Moveable entity abstract class
 *
 * @see Entity
 * @see Enemy
 * @see Player
 */
public abstract class MoveableBody extends Entity implements BodyComponent {
    public BodyComposite getComposite() {
        return composite;
    }

    /**
     * name of moving object
     */

    protected BodyComposite composite;

    private final StringProperty name = new SimpleStringProperty(this, "name", "°-°");
    private Text nameText;
    protected double actualSpeedX = 0;
    protected double actualSpeedY = 0;
    public static final double BASE_MAX_SPEED = Constants.getBaseMaxSpeed();
    public static final double ENEMY_SPEED_MULTIPLIER = Constants.getEnemySpeedMultiplier();
    public static final double CLONE_SPLIT_DISTANCE = Constants.getCloneSplitDistance();
    protected double speedMultiplier = 1.0;

    /**
     * @param group       the group on which the entity is moving
     * @param initialSize the moveable entity's initial size
     * @param color       the moveable entity's color
     * @see Entity
     */
    public MoveableBody(Group group, double initialSize, Color color) {
        super(group, initialSize, color);
        initializeNameText(group);
        this.composite = new BodyComposite(this);
    }

    /**
     * @param group       the group on which the entity is moving
     * @param id          the moveable entity's id
     * @param initialSize the moveable entity's initial size
     * @param color       the moveable entity's color
     * @see Entity
     */
    public MoveableBody(Group group, String id, double initialSize, Color color) {
        super(group, id, initialSize, color);
        initializeNameText(group);
        this.composite = new BodyComposite(this);
    }

    public MoveableBody(Group group, String id, double initialSize, Color color, String playerName) {
        super(group, id, initialSize, color);
        this.name.set(playerName);
        initializeNameText(group);
        this.composite = new BodyComposite(this);
    }

    /**
     * @param group       the group on which the entity is moving
     * @param initialSize the moveable entity's initial size
     * @param color       the moveable entity's color
     * @param name        the moveable entity's name
     * @see Entity
     */
    public MoveableBody(Group group, double initialSize, Color color, String name) {
        super(group, initialSize);
        this.name.set(name);
        sprite.setFill(color);
        initializeNameText(group);
        this.composite = new BodyComposite(this);
    }

    /**
     * @param group       the group on which the entity is moving
     * @param initialSize the moveable entity's initial size
     * @param name        the moveable entity's name
     * @see Entity
     */
    public MoveableBody(Group group, double initialSize, String name) {
        super(group, initialSize);
        this.name.set(name);
        initializeNameText(group);
        this.composite = new BodyComposite(this);
    }

    @Override
    public void addClone(BodyComponent clone) {
        composite.addClone(clone);
    }

    @Override
    public void removeClone(BodyComponent clone) {
        composite.removeClone(clone);
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    // Modify the splitSprite method
    public abstract void splitSprite();

    /**
     * initialize the name of the entity to be properly displayed on the sprite
     *
     * @param group the plan on which the moving entity is created
     * @see Player
     * @see Enemy
     */
    private void initializeNameText(Group group) {
        nameText = new Text();
        nameText.textProperty().bind(name);
        nameText.setFill(Color.BLACK);
        nameText.setStyle("-fx-font-size: 14;");
        // Place the text above the sprite in the render
        nameText.setViewOrder(-1000);

        nameText.styleProperty().bind(Bindings.createStringBinding(
                () -> String.format("-fx-font-size: %.1f;", sprite.getRadius()),
                sprite.radiusProperty()
        ));

        nameText.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            nameText.setX(sprite.getCenterX() - newBounds.getWidth() / 2);
        });
        nameText.yProperty().bind(sprite.centerYProperty());

        sprite.centerXProperty().addListener((obs, oldX, newX) -> {
            nameText.setX(newX.doubleValue() - nameText.getLayoutBounds().getWidth() / 2);
        });
        group.getChildren().add(nameText);
    }


    /**
     * increase the size of a moving entity
     *
     * @param foodValue the mass of the entity eaten
     */
    public void increaseSize(double foodValue) {
        setMasse(getMasse() + foodValue);
    }

    /**
     * move the entity toward a direction
     *
     * @param velocity the target direction
     */
    public void moveToward(double[] velocity) {
        // direction vector
        double[] direction = new double[]{
                velocity[0] - sprite.getCenterX(),
                velocity[1] - sprite.getCenterY()
        };

        // distance between target and entity's center
        double distanceFromCenter = Math.sqrt(
                direction[0] * direction[0] +
                        direction[1] * direction[1]
        );

        // normalization of direction vector
        double[] normalizedDirection = normalizeDouble(direction);

        // dead zone in the entity center (don't trigger movement)
        if (distanceFromCenter <= 10) {
            actualSpeedX = 0;
            actualSpeedY = 0;
            return;
        }

        calculateSpeeds(distanceFromCenter);

        double dx = normalizedDirection[0] * actualSpeedX;
        double dy = normalizedDirection[1] * actualSpeedY;

        // update position within the limit of the map
        if (sprite.getCenterX() + dx < MAP_LIMIT_WIDTH && sprite.getCenterX() + dx > -MAP_LIMIT_WIDTH) {
            sprite.setCenterX(sprite.getCenterX() + dx);
        }
        if (sprite.getCenterY() + dy < MAP_LIMIT_HEIGHT && sprite.getCenterY() + dy > -MAP_LIMIT_HEIGHT) {
            sprite.setCenterY(sprite.getCenterY() + dy);
        }
    }
    
    public boolean isAlive() {
        return getSprite().getParent() != null;
    }

    /**
     * calculate the speed at which the entity is moving
     * @param distanceFromCenter the distance bewteen the center of the entity and the target direction
     */
    protected abstract void calculateSpeeds(double distanceFromCenter);

    public double getMaxSpeed() {
        return BASE_MAX_SPEED / (1 + Math.log10(getMasse())) * speedMultiplier;
    }


    /**
     * calculates the distance between the moving object and another entity
     *
     * @param position position of the other entity as a double array
     * @return the distance between the center of the moving object and the other entity
     */
    public double distanceTo(double[] position) {
        return Math.sqrt(Math.pow(position[0] - getPosition()[0], 2) + Math.pow(position[1] - getPosition()[1], 2));
    }

    /**
     * transform an array of double in a vector if possible
     *
     * @param array the array being transformed
     * @return the array as a vector if successful transformation, a (0; 0) vector otherwise
     */
    public double[] normalizeDouble(double[] array) {

        double magnitude = Math.sqrt((array[0] * array[0]) + (array[1] * array[1]));

        if (array[0] != 0 || array[1] != 0) {
            return new double[]{array[0] / magnitude, array[1] / magnitude};
        }
        return new double[]{0, 0};
    }

    /**
     * set the speed multiplier of the moveable entity
     * @param multiplier the speed multiplier
     */
    public void setSpeedMultiplier(double multiplier) {
        this.speedMultiplier = multiplier;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDeletion() {
        super.onDeletion();
        deleteText();
    }

    /**
     * removes the name of the entity from the visible plan
     */
    public void deleteText() {
        if (nameText.getParent() != null) {
            ((Group) nameText.getParent()).getChildren().remove(nameText);
        }
    }

    /**
     * {@return the moveable entity's name}
     */
    public String getNom() {
        return name.get();
    }


    public void setNom(String nom) {
        this.name.set(nom);
    }

    /**
     * {@return the entity's speed in the x-axis}
     */
    public double getActualSpeedX() {
        return actualSpeedX;
    }

    /**
     * {@return the entity's speed in the y-axis}
     */
    public double getActualSpeedY() {
        return actualSpeedY;
    }

    @Override
    public boolean belongsToSameComposite(BodyComponent other) {
        if (other instanceof MoveableBody) {
            return composite == ((MoveableBody) other).composite;
        }
        return false;
    }

    public void setComposite(BodyComposite composite) {
        this.composite = composite;
    }

    public double getTotalMasse() {
        if (composite.isComposite()) {
            // Calculer la masse totale du composite
            double totalMasse = super.getMasse(); // Masse du corps principal
            for (BodyComponent clone : composite.getClones()) {
                if (clone instanceof MoveableBody) {
                    totalMasse += ((MoveableBody) clone).getMasse();
                }
            }
            return totalMasse;
        }
        return super.getMasse();
    }
}