package com.example.sae.core.entity.powerUp;

import com.example.sae.core.entity.MoveableBody;
import javafx.scene.paint.Color;

/**
 * Enumeration defining the different possible power-up
 * @see PowerUp
 */
public enum PowerUpType {
    /// power-up increasing the speed
    SPEED_BOOST(Color.BLUE, 5, 2) {
        @Override
        public void applyEffect(MoveableBody body) {
            body.setSpeedMultiplier(1.5);
        }

        @Override
        public void removeEffect(MoveableBody body) {
            body.setSpeedMultiplier(1.0);
        }
    },

    /// power-up decreasing speed
    SPEED_DECREASE(Color.RED, 5, 2) {
        @Override
        public void applyEffect(MoveableBody body) {
            body.setSpeedMultiplier(0.5);
        }

        @Override
        public void removeEffect(MoveableBody body) {
            body.setSpeedMultiplier(1.0);
        }
    },

    /// power-up splitting the target body
    SPLIT(Color.GREEN, 0, 100) {
        @Override
        public void applyEffect(MoveableBody body) {
            body.splitSprite();
            body.splitSprite();
        }

        @Override
        public void removeEffect(MoveableBody body) {
            // Split is instantaneous, no need for removal
        }
    };

    /// the power-up sprite color
    private final Color color;
    /// the power-up duration
    private final int duration;
    /// the power-up mass
    private final double mass;

    /**
     * @param color the power-up color
     * @param duration the power-up duration
     * @param mass the power-up mass
     */
    PowerUpType(Color color, int duration, int mass) {
        this.color = color;
        this.duration = duration;
        this.mass = mass;
    }

    /**
     * {@return a randomly chosen power-up}
     */
    public static PowerUpType getRandomPowerUpType() {
        return values()[(int) (Math.random() * values().length)];
    }

    /**
     * {@return the power-up color}
     */
    public Color getColor() {
        return color;
    }

    /**
     * {@return the power-up duration}
     */
    public int getDuration() {
        return duration;
    }

    /**
     * {@return the power-up mass}
     */
    public double getMasse() {
        return mass;
    }

    /**
     * apply the power-up effect to a moving entity
     * @param body the entity affected by the power-up
     */
    public abstract void applyEffect(MoveableBody body);

    /**
     * remove the power-up effect of a moving entity
     * @param body the entity affected by the power-up
     */
    public abstract void removeEffect(MoveableBody body);
}