package com.example.sae.core.entity;

import com.example.sae.client.controller.SoloController;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class EntityFactory {
    private static Group root = SoloController.root;

    private static final List<String> ENEMY_NAMES = List.of(
            "Michel", "Roberto", "Camou", "Bricoti", "Skibidi",
            "Boom", "YouAreDead", "ez", "DimitriDu14", "LePafDeCamou",
            "RouxQuiRoule", "ViveLaPizzAnanas", "LeBurgerDuRU", "KiriGouter",
            "LeFouDuMétro", "Hank", "CoucouLesCopains", "Split=Friend",
            "Split=Enemy", "Me+YourMom=You"
    );
    public EntityFactory(Group root) {
        EntityFactory.root = root;
    }

    public static void setRoot(Group root) {
        EntityFactory.root = root;
    }

    public static Food createFood(double size) {
        return new Food(root, size);
    }

    public static Player createPlayer(double mass, Color color, boolean isLocal) {
        return new Player(root, mass, color, isLocal);
    }
    public static Player createPlayer(double mass, Color color) {
        return new Player(root, mass, color);
    }

    public static Enemy createEnemy(double mass) {
        return new Enemy(root, mass, getRandomName());
    }

    private static String getRandomName() {
        return ENEMY_NAMES.get((int) (Math.random() * ENEMY_NAMES.size()));
    }
}