package com.example.sae.client.utils.timer;

import com.example.sae.client.Client;
import javafx.animation.AnimationTimer;

/**
 * Managed the inGameTimer
 */
public class GameTimer extends AnimationTimer {
    /// The Client
    private final Client client;
    /// The FPS number of the game
    public static final double FPS = 120;
    /// set the frequency of refreshment
    private final double interval = 1000000000 / FPS;
    /// The last time the game update
    private double last = 0;

    /// Is true if the game is stopped
    private boolean stopped = false;

    /**
     * constructor
     * @param client Client
     */
    public GameTimer(Client client) {
        this.client = client;
    }

    /**
     * Stoped the game timer
     */
    @Override
    public void stop(){
        super.stop();
        stopped = true;
    }

    /**
     * Managed the game timer for updating
     * @param now
     *            The timestamp of the current frame given in nanoseconds. This
     *            value will be the same for all {@code AnimationTimers} called
     *            during one frame.
     */
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