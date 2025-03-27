package com.example.sae.client.utils.handler;

import javafx.scene.input.MouseEvent;

public interface MouseEventHandler {
    void handleMouseMove(MouseEvent e);
    double[] getMousePosition();
}