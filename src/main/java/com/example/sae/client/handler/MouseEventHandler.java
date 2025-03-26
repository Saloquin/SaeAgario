package com.example.sae.client.handler;

import javafx.scene.input.MouseEvent;

public interface MouseEventHandler {
    void handleMouseMove(MouseEvent e);
    double[] getMousePosition();
}