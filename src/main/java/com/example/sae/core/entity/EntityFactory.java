package com.example.sae.core.entity;

import com.example.sae.client.controller.SoloController;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
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
    private static Group root = SoloController.root;

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
     * @param color player color
     */
    public static Player createPlayer(double mass, String playerName, Color color) {
        return new Player(root, mass, color, playerName);
    }

    /**
     * create a moving object for a player
     *
     * @see Entity
     * @see MoveableBody
     * @see Player
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param id entity id
     * @param x coordinate of moving object
     * @param y coordinate of moving object
     * @param mass mass of the player's moving object
     * @param playerName name of the player's moving object
     * @param color player color
     */
    public static Player createPlayer(String id, double x, double y, double mass, String playerName, Color color) {
        return new Player(root, id, x, y, mass, color, playerName);
    }

    /**
     * create a moving object for a player
     *
     * @see MoveableBody
     *
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param mass mass of the player's moving object
     * @param color player color
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
}