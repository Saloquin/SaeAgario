package com.example.sae.client.controller;

import com.example.sae.client.AgarioApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
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

    @Override
    public void initialize(URL location, ResourceBundle resources){
        onlinePlay.setManaged(false);
        localPlay.setManaged(false);
    }

    private void toggleButtonVisibility(Button button){
        button.setVisible(!button.isVisible());
        button.setManaged(button.isVisible());
        button.setDisable(!button.isVisible());
    }

    private void gameButtonClicked()
    {
        toggleButtonVisibility(onlinePlay);
        toggleButtonVisibility(localPlay);
        toggleButtonVisibility(start);
    }

    public void onStart(ActionEvent event)
    {
        gameButtonClicked();
        onlinePlay.requestFocus();
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


    public void onOnlinePlay(ActionEvent event)
    {
        gameButtonClicked();
        start.requestFocus();
    }

    public void onLocalPlay(ActionEvent actionEvent)
    {
        gameButtonClicked();
        start.requestFocus();
    }
}