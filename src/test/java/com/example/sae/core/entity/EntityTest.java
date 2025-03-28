package com.example.sae.core.entity;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private Player player;
    private Enemy enemy;
    private Food food;
    Group root = new Group();

    @BeforeEach
    void setup() {


        EntityFactory.setRoot(root);

        player = new Player(root, 10, Color.RED);
        enemy = new Enemy(root, 10, "Jean-Mi");
        food = new Food(root,"1",0,0,10,Color.GREEN);

    }

    @Test
    void getMassePlayer() {

        assertEquals(10, player.getMasse());
    }
    @Test
    void getMasseEnemy() {
        assertEquals(10,enemy.getMasse());
    }
    @Test
    void getMasseFood() {
        assertEquals(10,food.getMasse());
    }

    @Test
    void setMassePlayer() {
        player.setMasse(15);
        assertEquals(15,player.getMasse());
    }
    @Test
    void setMasseEnemy() {
        enemy.setMasse(15);
        assertEquals(15,enemy.getMasse());
    }
    @Test
    void setMasseFood() {
        food.setMasse(15);
        assertEquals(15,food.getMasse());
    }

    @Test
    void getPositionPlayer() {
        double[] position = player.getPosition();
        assertArrayEquals(new double[]{0,0},position);
    }
    @Test
    void getPositionEnemy() {
        double[] position = enemy.getPosition();
        assertNotNull(position);
    }
    @Test
    void getPositionFood() {
        double[] position = food.getPosition();
        assertNotNull(position);
    }





    @Test
    void getX() {
        assertEquals(0,player.getX());
    }

    @Test
    void getY() {
        assertEquals(0,player.getY());
    }

    @Test
    void getColor() {
        assertEquals(Color.RED,player.getColor());
    }

    @Test
    void getEntityId() {
        assertEquals("1",food.getEntityId());
    }

    @Test
    void onDeletion() {

        assertTrue(root.getChildren().contains(player));
        player.onDeletion();
        assertFalse(root.getChildren().contains(player));
    }
}