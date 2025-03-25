package com.example.sae.client.controller;

import com.example.sae.client.AgarioApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;


public class MenuController{

    private Stage stage;

    @FXML
    private Button startButton;
    @FXML
    private Button changeSkinButton;
    @FXML
    private Button changeNameButton;
    @FXML
    private Button exitButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleStartButton() throws IOException {
        try{
            // Charger l'interface depuis le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/game.fxml"));
            Parent root = loader.load();

            // Recupere le Game Controller
            GameController gameController = loader.getController();

            // Creation de la scene
            Scene scene = new Scene(root ,1280,720);
            stage.setTitle("Agario Game");
            stage.setScene(scene);

            stage.show();

            AgarioApplication.startGame(stage, gameController.getGameContainer());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeSkinButton() {
        System.out.println("Change Skin button clicked");
    }

    @FXML
    private void handleChangeNameButton() {
        System.out.println("Change Name button clicked");
    }

    @FXML
    private void handleExitButton() {
        Platform.exit();
    }


}