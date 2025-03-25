package com.example.sae.client;


import com.example.sae.client.controller.MenuController;
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
    private static Client client;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/menu.fxml"));

        // TODO : Update code to remove load before scene change
        // AnchorPane menuRoot = loader.load();
        // MenuController menuController = loader.getController();
        // menuController.setStage(stage);

        scene = new Scene(loader.load(), 250, 500);
        stage.setTitle("Agar.io");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void startGame(Stage stage) throws IOException {
        client = new Solo(root);
        client.init();
        scene = client.createGameScene(1280, 720);
        stage.setScene(scene);
    }

    public static Client getClient() {
        return client;
    }

    public static void main(String[] args) {
        launch();
    }
}