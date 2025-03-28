package com.example.sae.core.quadtree;

import junit.framework.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoundaryTest {

    private Boundary boundaryTest;

    @BeforeEach
    void setUp() {
        boundaryTest = new Boundary(0.0,0.0,1.0,1.0);
    }

    private static Stream<Arguments> pointsDeliverer() {
        return Stream.of(
                Arguments.of(true, 0.5, 0.5),
                Arguments.of(false, 0.5, -1.5),
                Arguments.of(true, 0.0, 0.0)
        );
    }

    private static Stream<Arguments> boundariesDeliverer() {
        return Stream.of(
                Arguments.of(true, new Boundary(0.5,0.5,0.2,0.2)),
                Arguments.of(false, new Boundary(2.5,2.5,1.0,1.0)),
                Arguments.of(true, new Boundary(0.0,0.0,1.0,1.0))
        );
    }

    @ParameterizedTest
    @MethodSource( "pointsDeliverer" )
    void containsTest(boolean target, double x, double y) {
        assertEquals(target, boundaryTest.contains(x, y));
    }

    @ParameterizedTest
    @MethodSource( "boundariesDeliverer" )
    void intersectsTest(boolean target, Boundary boundary) {
        assertEquals(target, boundaryTest.intersects(boundary));
    }

    @Test
    void getXTest() {
        assertEquals(0.0,boundaryTest.getX());
    }

    @Test
    void getYTest() {
        assertEquals(0.0,boundaryTest.getY());
    }

    @Test
    void getWidthTest() {
        assertEquals(1.0,boundaryTest.getWidth());
    }

    @Test
    void getHeightTest() {
        assertEquals(1.0,boundaryTest.getHeight());
    }
}