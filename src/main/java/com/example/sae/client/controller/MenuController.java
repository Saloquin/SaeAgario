package com.example.sae.client.controller;

import com.example.sae.client.controller.template.Dialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    @FXML
    private TextField playerNameField;
    @FXML
    private Button start;
    @FXML
    private Button onlinePlay;
    @FXML
    private Button localPlay;
    @FXML
    private Button changeSkin;

    @FXML
    private Button exit;

    static Stage mainStage;


    @FXML
    private ColorPicker playerColorPicker;
    private Color playerColor = Color.RED;


    private String playerName = "Player";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerNameField.setText(playerName);
        playerNameField.textProperty().addListener((obs, old, newValue) -> {
            playerName = newValue.trim().isEmpty() ? "Player" : newValue.trim();
        });

        playerColorPicker.valueProperty().addListener((obs, old, newValue) -> {
            playerColor = newValue;
        });
    }

    private void lauchSoloGameWindow(boolean isOnline, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/solo.fxml"));
            SoloController controller = new SoloController();
            controller.setPlayerName(playerName);
            controller.setPlayerColor(playerColor);
            loader.setController(controller);

            Node currentNode = (Node) (event.getSource());

            Scene scene = new Scene(loader.load(), Screen.getPrimary().getBounds().getWidth()*0.9, Screen.getPrimary().getBounds().getHeight()*0.9);
            String title = "AgarIO - " + (isOnline ? "Online" : "Local");
            Stage stage = new Stage();
            mainStage = stage;
            stage.setTitle(title);
            stage.setScene(scene);
            stage.setMinHeight(500);
            stage.setMinWidth(700);

            currentNode.getScene().getWindow().hide();

            stage.showAndWait();

            controller.stopGame();

            ((Stage) currentNode.getScene().getWindow()).show();
        }
        catch(Exception e) {
            Dialog.alertWindow("An error occured", "An error occured while trying to start the game.", e.getMessage());
        }
    }

    public void onOnlinePlay(ActionEvent event) {
        //lauchGameWindow(true, event);
    }

    public void onLocalPlay(ActionEvent event) {
        lauchSoloGameWindow(false, event);
    }

    public void onChangeSkin(ActionEvent event) {
        System.out.println("Change Skin button clicked");
    }


    public void onExit(ActionEvent event) {
        Platform.exit();
    }

    private void toggleButtonVisibility(Button button) {
        button.setVisible(!button.isVisible());
        button.setManaged(button.isVisible());
        button.setDisable(!button.isVisible());
    }



}