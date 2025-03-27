package com.example.sae.core.entity.powerUp;

import com.example.sae.client.Solo;
import com.example.sae.client.utils.debug.DebugWindowController;
import com.example.sae.client.utils.timer.GameTimer;
import com.example.sae.core.entity.MoveableBody;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class EffectManager {
    private static final ConcurrentHashMap<MoveableBody, Effect> activeEffects = new ConcurrentHashMap<>();

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

    public void update() {
        activeEffects.forEach((body, effect) -> {
            if (!Solo.getGameEngine().getEntities().contains(body) || effect.update()) {
                effect.type.removeEffect(body);
                activeEffects.remove(body);
                DebugWindowController.addLog("Effet supprimé - Total effets actifs: " + activeEffects.size());

            }
        });
    }

    private static class Effect {
        private final PowerUpType type;
        private int remainingFrames;

        Effect(PowerUpType type) {
            this.type = type;
            this.remainingFrames = type.getDuration() * (int) GameTimer.FPS;
        }

        boolean update() {
            return --remainingFrames <= 0;
        }
    }
}