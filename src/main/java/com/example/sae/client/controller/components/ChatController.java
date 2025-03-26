package com.example.sae.client.controller.components;

import com.example.sae.client.ChatClient;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML
    private ListView<String> chatListView;
    @FXML
    private TextField chatInput;

    private ChatClient chatClient;

    public void initialize(String playerName) {
        chatClient = new ChatClient(playerName, message -> chatListView.getItems().add(message));
        chatClient.start();

        chatInput.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                sendMessage();
            }
        });
    }

    @FXML
    private void sendMessage() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            chatClient.sendMessage(message);
            chatInput.clear();
        }
    }
}