package com.example.sae.client.controller;

import com.example.sae.client.controller.template.Dialog;
import com.example.sae.client.utils.PreferencesManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.LoadException;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private TextField nameField;
    @FXML private ColorPicker colorPicker;

    private String playerName = "Player";
    private Color playerColor = Color.BLUE;

    static Stage mainStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Charger les préférences
        String[] prefs = PreferencesManager.loadPreferences();
        if (prefs != null) {
            playerName = prefs[0] != null ? prefs[0] : playerName;
            playerColor = prefs[1] != null ? PreferencesManager.parseColor(prefs[1]) : playerColor;
        }

        // Initialisation des champs
        nameField.setText(playerName);
        colorPicker.setValue(playerColor);

        // Configuration des écouteurs
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            playerName = newVal;
            savePreferences();
        });

        colorPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            playerColor = newVal;
            savePreferences();
        });
    }

    private void savePreferences() {
        PreferencesManager.savePreferences(playerName, playerColor);
    }

    public void onOnlinePlay(ActionEvent event) {
        launchOnlineGameWindow(event);
    }

    @FXML
    public void onLocalPlay(ActionEvent event) {
        launchSoloGameWindow(event);
    }

    public void onExit(ActionEvent event) {
        Platform.exit();
    }


    private void launchSoloGameWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/solo.fxml"));
            SoloController controller = new SoloController();
            controller.setPlayerName(playerName);
            controller.setPlayerColor(playerColor);
            loader.setController(controller);

            Node currentNode = (Node) (event.getSource());
            Scene scene = new Scene(loader.load(),
                    Screen.getPrimary().getBounds().getWidth()*0.9,
                    Screen.getPrimary().getBounds().getHeight()*0.9);

            Stage stage = new Stage();
            mainStage = stage;
            stage.setTitle("AgarIO - Local");
            stage.setScene(scene);
            stage.setMinHeight(500);
            stage.setMinWidth(700);

            Stage currentStage = (Stage) currentNode.getScene().getWindow();
            currentStage.hide();

            stage.showAndWait();
            currentStage.show();
        }
        catch(Exception e) {
            Dialog.alertWindow("Error", "Failed to start game", e.getMessage());
        }
    }

    private void launchOnlineGameWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/online.fxml"));
            OnlineController controller = new OnlineController();
            controller.setPlayerName(playerName);
            controller.setPlayerColor(playerColor);
            loader.setController(controller);

            Node currentNode = (Node) (event.getSource());
            Scene scene = new Scene(loader.load(),
                    Screen.getPrimary().getBounds().getWidth()*0.9,
                    Screen.getPrimary().getBounds().getHeight()*0.9);

            Stage stage = new Stage();
            mainStage = stage;
            stage.setTitle("AgarIO - Online");
            stage.setScene(scene);
            stage.setMinHeight(500);
            stage.setMinWidth(700);

            Stage currentStage = (Stage) currentNode.getScene().getWindow();
            currentStage.hide();

            stage.showAndWait();
            // currentStage.show();
        } catch (LoadException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Server not found", ButtonType.OK);
            alert.showAndWait();
        } catch(Exception e) {
            Dialog.alertWindow("Error", "Failed to start game", e.getMessage());
        }
    }

    // Getters pour récupérer les valeurs dans d'autres contrôleurs
    public String getPlayerName() {
        return playerName;
    }

    public Color getPlayerColor() {
        return playerColor;
    }
}