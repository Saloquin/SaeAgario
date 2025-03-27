package com.example.sae.core.entity;



import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

/**
 * static object/entity, food that helps a moving object/entity gain mass
 *
 * @see Entity
 * @see MoveableBody
 *
 * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
 */
public class Food extends Entity{

    /**
     * constructor
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param size food size
     */
    public Food(Group group, double size){
        super(group, size);
        sprite.setCenterX(Math.random() * (MAP_LIMIT_WIDTH * 2) - MAP_LIMIT_WIDTH);
        sprite.setCenterY(Math.random() * (MAP_LIMIT_HEIGHT * 2) - MAP_LIMIT_HEIGHT);

    }

    /**
     * constructor
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     * @param group Group
     * @param id entity id
     * @param x food coordinate
     * @param y food coordinate
     * @param size food size
     * @param color food color
     */
    public Food(Group group, String id, double x, double y, double size, Color color) {
        super(group, id, size, color);
        sprite.setCenterX(x);
        sprite.setCenterY(y);
    }
    /**
     * not used in this class, empty
     *
     * @see Entity
     *
     * @author Elsa HAMON - Paul LETELLIER - Camille GILLE - Thomas ROGER - Maceo DAVID - Clemence PAVY
     */

    @Override
    public void Update() {

    }
}
