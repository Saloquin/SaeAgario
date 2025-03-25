package com.example.sae.client.controller;

import com.example.sae.client.controller.template.Dialog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class MenuController implements Initializable {

    private Stage stage;

    @FXML
    private Button start;
    @FXML
    private Button onlinePlay;
    @FXML
    private Button localPlay;
    @FXML
    private Button changeSkin;
    @FXML
    private Button changeName;
    @FXML
    private Button exit;

    /*
    Override of the initialize function to prevent hidden buttons from taking place in the UI
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        onlinePlay.setManaged(false);
        localPlay.setManaged(false);
    }

    public void onStart(ActionEvent event)
    {
        toggleGameButtonsVisibility();
        onlinePlay.requestFocus();
    }

    public void onOnlinePlay(ActionEvent event)
    {
        toggleGameButtonsVisibility();
        start.requestFocus();
        lauchGameWindow();
    }

    public void onLocalPlay(ActionEvent actionEvent)
    {
        toggleGameButtonsVisibility();
        start.requestFocus();
        lauchGameWindow();
    }

    public void onChangeSkin(ActionEvent event)
    {
        System.out.println("Change Skin button clicked");
    }

    public void onChangeName(ActionEvent event)
    {
        System.out.println("Change Name button clicked");
    }

    public void onExit(ActionEvent event)
    {
        Platform.exit();
    }

    /**
     * Toggle the visibility of a button
     * @param button the button being toggled
     */
    private void toggleButtonVisibility(Button button){
        button.setVisible(!button.isVisible());
        button.setManaged(button.isVisible());
        button.setDisable(!button.isVisible());
    }

    /**
     * Toggle the visibility of the buttons related to the launch of the game
     */
    private void toggleGameButtonsVisibility()
    {
        toggleButtonVisibility(onlinePlay);
        toggleButtonVisibility(localPlay);
        toggleButtonVisibility(start);
    }

    private void lauchGameWindow()
    {
        try
        {
            Stage stage = new Stage();
        }
        catch(Exception e)
        {
            Dialog.alertWindow("An error occured", "An error occured while trying to start the game.", e.getMessage());
        }
    }
}