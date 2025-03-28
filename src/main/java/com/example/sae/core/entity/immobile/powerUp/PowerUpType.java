package com.example.sae.core.entity.immobile.powerUp;
import com.example.sae.client.utils.config.Constants;
import com.example.sae.core.entity.movable.body.MoveableBody;
import javafx.scene.paint.Color;

public enum PowerUpType {
    SPEED_BOOST(Color.BLUE, 5,2) {
        @Override
        public void applyEffect(MoveableBody body) {
            body.setSpeedMultiplier(1.5);
        }

        @Override
        public void removeEffect(MoveableBody body) {
            body.setSpeedMultiplier(1.0);
        }
    },

    SPEED_DECREASE(Color.RED, 5,2) {
        @Override
        public void applyEffect(MoveableBody body) {
            body.setSpeedMultiplier(0.5);
        }

        @Override
        public void removeEffect(MoveableBody body) {
            body.setSpeedMultiplier(1.0);
        }
    },

    SPLIT(Color.GREEN, 0,100) {
        @Override
        public void applyEffect(MoveableBody body) {
            body.splitSprite();
        }

        @Override
        public void removeEffect(MoveableBody body) {
            // Split is instantaneous, no need for removal
        }
    };

    private final Color color;
    private final int duration;
    private final double masse;

    PowerUpType(Color color, int duration, int masse) {
        this.color = color;
        this.duration = duration;
        this.masse = masse;
    }

    public static PowerUpType getRandomPowerUpType() {
        return values()[(int) (Math.random() * values().length)];
    }

    public Color getColor() {
        return color;
    }

    public int getDuration() {
        return duration;
    }
    public double getMasse() {
        return masse;
    }

    public abstract void applyEffect(MoveableBody body);
    public abstract void removeEffect(MoveableBody body);
}