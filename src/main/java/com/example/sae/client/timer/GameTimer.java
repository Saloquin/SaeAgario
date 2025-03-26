package com.example.sae.client.timer;

import com.example.sae.client.Client;
import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
    private final Client client;
    private final double framesPerSecond = 120;
    private final double interval = 1000000000 / framesPerSecond;
    private double last = 0;

    public GameTimer(Client client) {
        this.client = client;
    }

    @Override
    public void handle(long now) {
        if (last == 0) {
            last = now;
        }

        if (now - last > interval && client.getGameStarted()) {
            last = now;
            client.update();
        }
    }
}