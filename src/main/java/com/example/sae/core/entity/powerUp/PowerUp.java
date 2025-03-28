package com.example.sae.core.entity.powerUp;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.MoveableBody;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class PowerUp extends Entity {
    protected PowerUpType type;
    protected static final long DURATION = 5000; // 5 seconds in milliseconds

    public PowerUp(Group group, PowerUpType type) {
        super(group, type.getMasse(), type.getColor());
        this.type = type;
        sprite.setStroke(Color.BLACK);
        sprite.setStrokeWidth(2);
        sprite.setCenterX(Math.random() * (MAP_LIMIT_WIDTH * 2) - MAP_LIMIT_WIDTH);
        sprite.setCenterY(Math.random() * (MAP_LIMIT_HEIGHT * 2) - MAP_LIMIT_HEIGHT);
    }

    public PowerUp(Group group, String id, double x, double y, double size, Color color,String name) {
        super(group, id, size, color);
        type = PowerUpType.valueOf(name);
        sprite.setCenterX(x);
        sprite.setCenterY(y);
        sprite.setStroke(Color.BLACK);
        sprite.setStrokeWidth(2);
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