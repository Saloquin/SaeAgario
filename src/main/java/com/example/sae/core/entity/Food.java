package com.example.sae.core.entity;


import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;


public class Food extends Entity{


    public Food(Group group, double size) {
        super(group, size);
        Sprite.setCenterX(Math.random() * (MAP_LIMIT_WIDTH * 2) - MAP_LIMIT_WIDTH);
        Sprite.setCenterY(Math.random() * (MAP_LIMIT_HEIGHT * 2) - MAP_LIMIT_HEIGHT);
    }

    public Food(Group group, double x, double y, double size, Color color) {
        super(group, size, color);
        Sprite.setCenterX(x);
        Sprite.setCenterY(y);
    }
}
