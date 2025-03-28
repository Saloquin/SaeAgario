package com.example.sae.core.quadtree;

import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuadTreeTest {

    private static QuadTree tree;
    private static Food food;

    @BeforeAll
    static void setUpBefore() throws Exception {
        food = new Food(new Group(), "id", -0.5, -0.5, 1.0, Color.BISQUE);
        tree = new QuadTree(new Boundary(0.0,0.0,1.0,1.0), 2);
    }

    @Test
    @Order(1)
    void subdivideTest() {
        tree.subdivide();
        assertEquals(new QuadTree(new Boundary(-0.5,-0.5,0.5,0.5), 2), tree.getNorthwest());
        assertEquals(new QuadTree(new Boundary(-0.5,0.5,0.5,0.5), 2), tree.getSouthwest());
        assertEquals(new QuadTree(new Boundary(0.5,-0.5,0.5,0.5), 2), tree.getNortheast());
        assertEquals(new QuadTree(new Boundary(0.5,0.5,0.5,0.5),2), tree.getSoutheast());
    }

    @Test
    @Order(2)
    void insertTest() {
        boolean insert = tree.insert(food);
        assertTrue(insert);
    }

    @Test
    @Order(3)
    void queryTest() {
        List<Entity> foods = new ArrayList<>(tree.query(new Boundary(-0.5,-0.5,0.5,0.5)));
        ArrayList<Entity> expectedFoods = new ArrayList<>();
        expectedFoods.add(food);
        assertEquals(expectedFoods, foods);
    }

    @Test
    @Order(4)
    void removeTest() {
        boolean remove = tree.remove(food);
        assertTrue(remove);
    }

    @Test
    void queryTestNoFoods() {
        List<Entity> foods = new ArrayList<>(tree.query(new Boundary(9.0,-0.5,0.5,0.5)));
        ArrayList<Entity> expectedFoods = new ArrayList<>();
        assertEquals(expectedFoods, foods);
    }
}