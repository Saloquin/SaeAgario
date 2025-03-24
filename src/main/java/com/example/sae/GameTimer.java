package com.example.sae;

import com.example.sae.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;

import static com.example.sae.AgarioApplication.root;

/**
public class GameTimer extends AnimationTimer {

    private double framesPerSecond = 120;
    private double interval = 1000000000 / framesPerSecond;
    private double last = 0;
    private AgarioApplication app;

    public GameTimer(AgarioApplication app){
        super();
        this.app = app;
    }
    @Override
    public void handle(long now) {

        if (last == 0){
            last = now;
        }

        if (now - last > interval ){
            if (AgarioApplication.gameStarted){
                last = now;
                app.freeQueuedObjects(); // deletes any objects queued up to be free
                for (Node entity : root.getChildren()){
                    Entity convertedEntity = (Entity) entity;
                    convertedEntity.Update();
                }
                app.Update(); //calls update function every frame
            }

        }
    }
}
*/