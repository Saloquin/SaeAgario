package com.example.sae.client.factory;

import com.example.sae.client.Client;
import com.example.sae.client.Solo;
import com.example.sae.core.entity.EntityFactory;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class GamePaneFactoryTest {

    @Test
    @DisplayName("Test Creation Game Pane")
    void createGamePane() {

        Group root = new Group();

        EntityFactory.setRoot(root);

        Client client = new Solo(root, "Jean-mi", Color.RED);


        Pane pane = client.createGamePane();
        assertNotNull(pane);
        assertEquals(pane.getChildren().get(0), root);




    }
}