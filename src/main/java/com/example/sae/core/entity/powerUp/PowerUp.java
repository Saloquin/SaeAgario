package com.example.sae.core.entity.powerUp;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.MoveableBody;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

/**
 * unmovable entity on the game granting special effects
 *
 * @see PowerUpType
 * @see Entity
 */
public class PowerUp extends Entity {
    /// the power-up type
    protected PowerUpType type;

    /**
     * @param group the plan on which the power-up is created
     * @param type the power-up type
     */
    public PowerUp(Group group, PowerUpType type) {
        super(group, type.getMasse(), type.getColor());
        this.type = type;
        sprite.setStroke(Color.BLACK);
        sprite.setStrokeWidth(2);
        sprite.setCenterX(Math.random() * (MAP_LIMIT_WIDTH * 2) - MAP_LIMIT_WIDTH);
        sprite.setCenterY(Math.random() * (MAP_LIMIT_HEIGHT * 2) - MAP_LIMIT_HEIGHT);
    }

    /**
     * apply the power-up effect to a moving entity
     * @param body the entity affected by the power-up
     */
    public void applyEffect(MoveableBody body) {
        EffectManager.applyEffect(body, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        // Empty as PowerUps don't move
    }
}