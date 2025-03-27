package com.example.sae.core.entity.player;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class PlayerPart extends Player {

    protected PlayerPart(Group group, double masse, Color color) {
        super(group, masse, color);
    }

    protected PlayerPart(Group group, String id, double x, double y, double masse, Color color, boolean isLocal){
        super(group, id, x, y, masse, color, isLocal);
    }

    public void split(){
        double masse = getMasse()/2;
        Group test = (Group) sprite.getParent();
        setMasse(masse);
        parts.add(new PlayerPart(test, getId(), sprite.getCenterX(), sprite.getCenterY(), masse, getColor(), isLocal()));
        System.out.println("LA VACA SATURNITO SATURNA");
    }

}
