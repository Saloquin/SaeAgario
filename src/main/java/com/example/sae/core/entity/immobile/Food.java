package com.example.sae.core.entity.immobile;



import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.movable.body.MoveableBody;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

/**
 * unmovable entity on the game
 *
 * @see Entity
 */
public class Food extends Entity {

    /**
     * @param group the plan on which the food is created
     * @param size  the food's size
     */
    public Food(Group group, double size) {
        super(group, size);
        sprite.setCenterX(Math.random() * (MAP_LIMIT_WIDTH * 2) - MAP_LIMIT_WIDTH-50);
        sprite.setCenterY(Math.random() * (MAP_LIMIT_HEIGHT * 2) - MAP_LIMIT_HEIGHT-50);

    }

    /**
     * @param group the plan on which the food is created
     * @param id    the food's entity id
     * @param x     the food's X coordinate
     * @param y     the food's Y coordinate
     * @param size  the food's size
     * @param color the food's color
     */
    public Food(Group group, String id, double x, double y, double size, Color color) {
        super(group, id, size, color);
        sprite.setCenterX(x);
        sprite.setCenterY(y);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public void Update() {

    }
}
