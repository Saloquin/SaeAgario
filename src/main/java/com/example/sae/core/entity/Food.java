package com.example.sae.core.entity;


import com.example.sae.client.AgarioApplication;
import javafx.scene.Group;


public class Food extends Entity{


    public Food(Group group, double size){
        // super(group == null ? new Group() : group, size);
        super(group, size);

        if (group != null) {
            Sprite.setCenterX(Math.random() * (AgarioApplication.getMapLimitWidth() * 2) - AgarioApplication.getMapLimitWidth());
            Sprite.setCenterY(Math.random() * (AgarioApplication.getMapLimitHeight() * 2) - AgarioApplication.getMapLimitHeight());
        }
    }
}
