package com.example.sae.client;


import com.example.sae.client.utils.config.ConfigLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 *
 */
public class AgarioApplication extends Application {
    public static final Group root = new Group();
    private static Scene scene;
    private static Client client;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ConfigLoader.loadConfig();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/menu.fxml"));

        scene = new Scene(loader.load(), 250, 500);
        stage.setTitle("agarIO - Menu");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}