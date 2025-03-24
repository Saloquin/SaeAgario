package com.example.sae;

import javafx.scene.Group;

public class Enemy extends MoveableBody{


    private double closestEntityDistance;
    private Entity closestEntity;

    Enemy(Group group, double masse){
        super(group, masse);
        Sprite.setCenterX((Math.random() * AgarioApplication.getMapLimitWidth() * 2) - AgarioApplication.getMapLimitWidth());
        Sprite.setCenterY((Math.random() * AgarioApplication.getMapLimitHeight() * 2) - AgarioApplication.getMapLimitHeight());

        //puts the Enemy infront of all the food
        //puts bigger entities in front of smaller entities
    }

    @Override
    public void Update(){
        //move player towards the mouse position
        
        closestEntityDistance = distanceTo(AgarioApplication.player.getPosition());
        closestEntity = AgarioApplication.player;




        AgarioApplication.root.getChildren().forEach(entity ->{
            if (entity instanceof MoveableBody){
                MoveableBody movingEntity = (MoveableBody) entity;
                if (entity != this){
                    if (distanceTo(movingEntity.getPosition()) < closestEntityDistance) {
                        closestEntityDistance = distanceTo(movingEntity.getPosition());
                        closestEntity = movingEntity;

                    }
                }
            }

        });

        moveToward(closestEntity.getPosition());
        //check if player is colliding with anything
        checkCollision();
    }

    @Override public void onDeletion(){
        AgarioApplication.enemies--;
    }
}
