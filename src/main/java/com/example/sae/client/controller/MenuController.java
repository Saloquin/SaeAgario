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
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML private TextField nameField;
    @FXML private ColorPicker colorPicker;

    private String playerName = "";
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
            // Récupérer l'IP du serveur depuis les préférences
            String serverIP = PreferencesManager.loadServerIP();
            if (serverIP == null || serverIP.isEmpty()) {
                // Si aucune IP n'est trouvée, on affiche une popup pour la demander
                TextInputDialog ipDialog = new TextInputDialog();
                ipDialog.setTitle("Server IP Address");
                ipDialog.setHeaderText("Server connection required");
                ipDialog.setContentText("Enter server IP:");
                Optional<String> result = ipDialog.showAndWait();
                if (result.isPresent() ) {
                    if(!result.get().trim().isEmpty()){
                        serverIP = result.get().trim();
                        PreferencesManager.saveServerIP(serverIP);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Server IP is required!", ButtonType.OK);
                        alert.showAndWait();
                        return;
                    }
                } else {
                    return;
                }
            }

            // On passe l'IP au contrôleur en ligne pour qu'il puisse l'utiliser pour se connecter
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sae/online.fxml"));
            OnlineController controller = new OnlineController();
            controller.setPlayerName(playerName);
            controller.setPlayerColor(playerColor);
            controller.setServerIP(serverIP); // Méthode à ajouter dans OnlineController
            loader.setController(controller);

            Node currentNode = (Node) event.getSource();
            Scene scene = new Scene(loader.load(),
                    Screen.getPrimary().getBounds().getWidth() * 0.9,
                    Screen.getPrimary().getBounds().getHeight() * 0.9);

            Stage stage = new Stage();
            mainStage = stage;
            stage.setTitle("AgarIO - Online");
            stage.setScene(scene);
            stage.setMinHeight(500);
            stage.setMinWidth(700);

            Stage currentStage = (Stage) currentNode.getScene().getWindow();
            currentStage.hide();

            stage.showAndWait();
            controller.stopGame();
            currentStage.show();
        } catch (LoadException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Server not found", ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
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

    public void onSettings(ActionEvent event) {
        // Récupérer l'adresse IP actuelle depuis les préférences
        String currentIP = PreferencesManager.loadServerIP();

        // Pré-remplir la popup avec l'IP actuelle (si présente)
        TextInputDialog ipDialog = new TextInputDialog(currentIP != null ? currentIP : "");
        ipDialog.setTitle("Server IP Settings");
        ipDialog.setHeaderText("Change the server IP address");
        ipDialog.setContentText("Enter server IP:");

        // Appliquer un style personnalisé
        ipDialog.getDialogPane().getStyleClass().add("text-input-dialog");

        // Charger le fichier CSS
        Scene scene = ipDialog.getDialogPane().getScene();
        Optional<String> result = ipDialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String newIP = result.get().trim();
            PreferencesManager.saveServerIP(newIP);
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "IP mis à jour : " + newIP, ButtonType.OK);
            confirmation.showAndWait();
        } else {
        }
    }


}