package com.example.sae.client.utils.timer;

import com.example.sae.client.Client;
import javafx.animation.AnimationTimer;

/**
 * Manages the in-game timer, calling the client's update method at a fixed frame rate.
 */
public class GameTimer extends AnimationTimer {

    /// The game client to update
    private final Client client;

    /// Frames per second
    public static final double FPS = 120;

    /// Interval between updates
    private final double interval = 1_000_000_000 / FPS;

    /// Time of the last update
    private double last = 0;

    /// True if the timer is stopped
    private boolean stopped = false;

    /**
     * Constructs a new GameTimer for the given client.
     *
     * @param client The game client to update
     */
    public GameTimer(Client client) {
        this.client = client;
    }

    /**
     * Stops the game timer.
     */
    @Override
    public void stop() {
        super.stop();
        stopped = true;
    }

    /**
     * Called every frame. Updates the game client if enough time has passed.
     *
     * @param now The timestamp of the current frame
     */
    @Override
    public void handle(long now) {
        if (!stopped) {
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
