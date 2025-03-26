package com.example.sae.core.entity.powerUp;

import com.example.sae.client.AgarioApplication;
import com.example.sae.client.Solo;
import com.example.sae.client.debug.DebugWindowController;
import com.example.sae.client.timer.GameTimer;
import com.example.sae.core.entity.MoveableBody;

import java.util.HashMap;
import java.util.Map;

public class EffectManager {
    private static final Map<MoveableBody, EffectData> activeEffects = new HashMap<>();
    private static class EffectData {
        int remainingFrames;
        PowerUpType type;

        EffectData(int frames, PowerUpType type) {
            this.remainingFrames = frames;
            this.type = type;
        }
    }
    public static void applyEffect(MoveableBody body,PowerUpType powerUpType) {
        powerUpType.applyEffect(body);
        activeEffects.put(body,new EffectData(powerUpType.getDuration() * (int) GameTimer.FPS, powerUpType));
    }

    public void update() {
        activeEffects.entrySet().removeIf(entry -> {
            MoveableBody body = entry.getKey();
            if(!Solo.getGameEngine().getEntities().contains(body)) {
                return true;
            }
            EffectData effectData = entry.getValue();
            int timeRemaining = effectData.remainingFrames - 1;
            PowerUpType type = effectData.type;
            if (timeRemaining <= 0) {
                type.removeEffect(body);
                return true;
            } else {
                activeEffects.put(body, new EffectData(timeRemaining, type));
                return false;
            }
        });
    }
}