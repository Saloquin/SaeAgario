package com.example.sae.core.entity.powerUp;

import com.example.sae.client.Solo;
import com.example.sae.client.utils.debug.DebugWindowController;
import com.example.sae.client.utils.timer.GameTimer;
import com.example.sae.core.entity.MoveableBody;

import java.util.concurrent.ConcurrentHashMap;

/**
 * the manager for the effect
 */
public class EffectManager {
    /// the list of current active effects
    private static final ConcurrentHashMap<MoveableBody, Effect> activeEffects = new ConcurrentHashMap<>();

    /**
     * apply a power-up to a moving entity
     * @param body the entity on which the power-up is applied
     * @param powerUpType the power-up type
     */
    public static void applyEffect(MoveableBody body, PowerUpType powerUpType) {
        // Remove existing effect of same type if present
        Effect oldEffect = activeEffects.get(body);
        if (oldEffect != null && oldEffect.type == powerUpType) {
            oldEffect.remainingFrames = powerUpType.getDuration() * (int) GameTimer.FPS;
            return;
        }

        powerUpType.applyEffect(body);
        activeEffects.put(body, new Effect(powerUpType));
        DebugWindowController.addLog("Effet supprimé - Total effets actifs: " + activeEffects.size());

    }

    /**
     * update all the active effects
     */
    public void update() {
        activeEffects.forEach((body, effect) -> {
            if (!Solo.getGameEngine().getEntities().contains(body) || effect.update()) {
                effect.type.removeEffect(body);
                activeEffects.remove(body);
                DebugWindowController.addLog("Effet supprimé - Total effets actifs: " + activeEffects.size());

            }
        });
    }

    /**
     * private class defining the effect
     */
    private static class Effect {
        /// the power-up type
        private final PowerUpType type;
        /// the remaining duration for the effect
        private int remainingFrames;

        /**
         * @param type the power-up type
         */
        Effect(PowerUpType type) {
            this.type = type;
            this.remainingFrames = type.getDuration() * (int) GameTimer.FPS;
        }

        /**
         * update the effect
         * @return true if the duration of the effect ended, false otherwise
         */
        boolean update() {
            return --remainingFrames <= 0;
        }
    }
}