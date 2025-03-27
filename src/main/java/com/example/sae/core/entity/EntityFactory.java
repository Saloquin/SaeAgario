package com.example.sae.core.entity;

import com.example.sae.client.controller.SoloController;
import com.example.sae.core.entity.powerUp.PowerUp;
import com.example.sae.core.entity.powerUp.PowerUpType;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * entity factory: create entities
 *
 * @see Entity
 * @see MoveableBody
 * @see Player
 * @see Enemy
 * @see Food
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class EntityFactory {
    private static Group root = SoloController.getRoot();

    /**
     * name list for IA, or default
     */
    private static final List<String> ENEMY_NAMES = List.of(
            "Michel", "Roberto", "Camou", "Bricoti", "Skibidi",
            "Boom", "YouAreDead", "ez", "DimitriDu14", "LePafDeCamou",
            "RouxQuiRoule", "ViveLaPizzAnanas", "LeBurgerDuRU", "KiriGouter",
            "LeFouDuMétro", "Hank", "CoucouLesCopains", "Split=Friend",
            "Split=Enemy", "Me+YourMom=You"
    );

    /**
     * constructor
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param root Group
     */
    public EntityFactory(Group root) {
        EntityFactory.root = root;
    }

    /**
     * changes root
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param root new root
     */
    public static void setRoot(Group root) {
        EntityFactory.root = root;
    }

    /**
     * creates a static entity, food
     *
     * @see Entity
     * @see Food
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param size food's size
     */
    public static Food createFood(double size) {
        return new Food(root, size);
    }

    /**
     * create a moving object for a player
     *
     * @see Entity
     * @see MoveableBody
     * @see Player
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param playerName name of the player's moving object
     * @param color Boolean that determines whether the player's mobile object is in the local or online game
     */
    public static Player createPlayer(double mass,String playerName, Color color) {
        return new Player(root, mass, color,playerName);
    }

    /**
     * create a moving object for a player
     *
     * @see MoveableBody
     *
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param mass mass of the player's moving object
     * @param color name of the player's moving object
     */
    public static Player createPlayer(double mass, Color color) {
        return new Player(root, mass, color);
    }

    /**
     * create an AI mobile object
     *
     * @see Entity
     * @see MoveableBody
     * @see Enemy
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param mass masse of IA
     */
    public static Enemy createEnemy(double mass) {
        return new Enemy(root, mass, getRandomName());
    }

    /**
     * gives a random name from a list of fixed names
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @return random name
     */
    private static String getRandomName() {
        return ENEMY_NAMES.get((int) (Math.random() * ENEMY_NAMES.size()));
    }

    public static PowerUp createIncreaseSpeedPowerUp() {
        return new PowerUp(root,PowerUpType.SPEED_BOOST);
    }
    public static PowerUp createDecreaseSpeedPowerUp() {
        return new PowerUp(root,PowerUpType.SPEED_DECREASE );
    }
    public static PowerUp createSplitPowerUp() {
        return new PowerUp(root,PowerUpType.SPLIT);
    }

    public static PowerUp createRandomPowerUp() {
        return new PowerUp(root,PowerUpType.getRandomPowerUpType());
    }

}