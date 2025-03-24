package com.example.sae.controller;

import com.example.sae.AgarioApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
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
        AgarioApplication.startGame(stage);
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