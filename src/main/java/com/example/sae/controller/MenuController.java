package com.example.sae.controller;

import com.example.sae.AgarioApplication;
import com.example.sae.GameTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        AgarioApplication.gameStarted = true;
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