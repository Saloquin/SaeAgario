package com.example.sae.client;


import com.example.sae.client.controller.MenuController;
import com.example.sae.core.Camera;
import com.example.sae.server.AgarioServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class AgarioApplication extends Application {
    public static final Group root = new Group();
    private static Scene scene;
    private static Client client;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/menu.fxml"));

        scene = new Scene(loader.load(), 250, 500);
        stage.setTitle("agarIO - Menu");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    static public double[] getMousePosition(){
        java.awt.Point mouse = java.awt.MouseInfo.getPointerInfo().getLocation();
        Point2D mousePos = root.screenToLocal(mouse.x, mouse.y);
        return new double[]{mousePos.getX(), mousePos.getY()};
    }

    public static Scene getScene() {
        return scene;
    }
}