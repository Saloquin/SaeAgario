package com.example.sae.client.controller.template;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Dialog {

    /**
     * Create and show a confirmation window and return a boolean according to the choice made by the user.
     * @param title the title of the window
     * @param headerContent the content put in the header
     * @param content the content put in the content section
     * @return true if the user's choice is yes, false otherwise
     */
    public static boolean confirmationWindow(String title, String headerContent, String content) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        confirmation.setTitle(title);
        confirmation.setHeaderText(headerContent);
        confirmation.setContentText(content);
        ButtonType choice = confirmation.showAndWait().get();
        return choice == ButtonType.YES;
    }

    /**
     * Create and show an alert window
     * @param title the title of the window
     * @param headerContent the content put in the header
     * @param content the content put in the content section
     */
    public static void alertWindow(String title, String headerContent, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerContent);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
