package com.example.sae.client.controller;

import com.example.sae.client.AgarioApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;


public class MenuController{

    private Stage stage;

    @FXML
    private Button startSoloButton;
    @FXML
    private Button startOnlineButton;
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
    private void handleStartSoloButton() throws IOException {
        AgarioApplication.startGame(stage, true);
    }

    @FXML
    private void handleStartOnlineButton() throws IOException {
        AgarioApplication.startGame(stage, false);
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