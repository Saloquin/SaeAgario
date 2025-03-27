package com.example.sae.client;


import com.example.sae.client.controller.MenuController;
import com.example.sae.core.Camera;
import com.example.sae.server.AgarioServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class AgarioApplication extends Application {
    public static final Group root = new Group();
    private static Scene scene;

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
}