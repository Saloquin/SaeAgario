package com.example.sae.core.entity;

import com.example.sae.client.controller.GameController;
import com.example.sae.client.controller.SoloController;
import com.example.sae.core.entity.powerUp.PowerUp;
import com.example.sae.core.entity.powerUp.PowerUpType;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Factory creating entities
 *
 * @see Entity
 */
public class EntityFactory {

    /// default names list
    private static final List<String> ENEMY_NAMES = List.of(
            "Michel", "Roberto", "Camou", "Bricoti", "Skibidi",
            "Boom", "YouAreDead", "ez", "DimitriDu14", "LePafDeCamou",
            "RouxQuiRoule", "ViveLaPizzAnanas", "LeBurgerDuRU", "KiriGouter",
            "LeFouDuMétro", "Hank", "CoucouLesCopains", "Split=Friend",
            "Split=Enemy", "Me+YourMom=You", "BRR BRR PATAPIM"
    );

    /// the plan of the factory
    private static Group root = GameController.getRoot();

    /**
     * @param root the plan on which the factory creates entities
     */
    public EntityFactory(Group root) {
        EntityFactory.root = root;
    }

    /**
     * set a new plan for the creation of the entities
     *
     * @param root the new group for the game
     */
    public static void setRoot(Group root) {
        EntityFactory.root = root;
    }

    /**
     * creates a food entity
     *
     * @param size the food's size
     * @see Food
     */
    public static Food createFood(double size) {
        return new Food(root, size);
    }

    /**
     * create a player entity
     *
     * @param mass       the initial mass of the created player
     * @param playerName the name of the created player
     * @param color      the color of the created player
     * @see Player
     */
    public static Player createPlayer(double mass, String playerName, Color color) {
        return new Player(root, mass, color, playerName);
    }

    /**
     * create a player entity
     *
     * @param mass       the initial mass of the created player
     * @param color      the color of the created player
     * @see Player
     */
    public static Player createPlayer(double mass, Color color) {
        return new Player(root, mass, color);
    }

    /**
     * create an enemy entity
     *
     * @param mass the initial mass of the created enemy
     * @see Enemy
     */
    public static Enemy createEnemy(double mass) {
        return new Enemy(root, mass, getRandomName());
    }

    /**
     * return a random name from the list
     * @return a random name
     */
    private static String getRandomName() {
        return ENEMY_NAMES.get((int) (Math.random() * ENEMY_NAMES.size()));
    }

    /**
     * create a random power up
     * @return the created power up
     */
    public static PowerUp createRandomPowerUp() {
        return new PowerUp(root, PowerUpType.getRandomPowerUpType());
    }

}