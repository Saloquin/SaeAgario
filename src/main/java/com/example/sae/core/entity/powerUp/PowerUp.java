package com.example.sae.core.entity.powerUp;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.MoveableBody;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

/**
 *
 */
public class PowerUp extends Entity {
    protected static final long DURATION = 5000; // 5 seconds in milliseconds
    protected PowerUpType type;

    public PowerUp(Group group, PowerUpType type) {
        super(group, type.getMasse(), type.getColor());
        this.type = type;
        sprite.setStroke(Color.BLACK);
        sprite.setStrokeWidth(2);
        sprite.setCenterX(Math.random() * (MAP_LIMIT_WIDTH * 2) - MAP_LIMIT_WIDTH);
        sprite.setCenterY(Math.random() * (MAP_LIMIT_HEIGHT * 2) - MAP_LIMIT_HEIGHT);
    }

    public void applyEffect(MoveableBody body) {
        EffectManager.applyEffect(body, type);
    }

    public PowerUpType getType() {
        return type;
    }

    @Override
    public void Update() {
        // Empty as PowerUps don't move
    }
}