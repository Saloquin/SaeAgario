package com.example.sae.client.utils.timer;

import com.example.sae.client.Client;
import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
    private final Client client;
    public static final double FPS = 120;
    private final double interval = 1000000000 / FPS;
    private double last = 0;

    private boolean stopped = false;

    public GameTimer(Client client) {
        this.client = client;
    }

    @Override
    public void stop(){
        super.stop();
        stopped = true;
    }

    @Override
    public void handle(long now) {
        if(!stopped){
            if (last == 0) {
                last = now;
            }

            if (now - last > interval && client.getGameStarted()) {
                last = now;
                client.update();
            }
        }

    }
}