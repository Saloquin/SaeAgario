package com.example.sae.client;

import com.example.sae.client.utils.PreferencesManager;
import javafx.application.Platform;
import com.example.sae.client.controller.SoloController;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    private static final String SERVER_IP = PreferencesManager.loadServerIP();
    private static final int SERVER_PORT = 55555;
    private PrintWriter out;
    private final Consumer<String> messageHandler;
    private final String playerName;

    public ChatClient(String playerName, Consumer<String> messageHandler) {
        this.playerName = playerName;
        this.messageHandler = messageHandler;
    }

    public void start() {
        new Thread(() -> {
            try {
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    String finalMessage = message;
                    Platform.runLater(() -> messageHandler.accept(finalMessage));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (out != null) {
            String formattedMessage = playerName + "|" + message;
            out.println(formattedMessage);
        }
    }

}