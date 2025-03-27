package com.example.sae.client;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

/**
 *
 */
public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 55555;
    private final Consumer<String> messageHandler;
    private final String playerName;
    private PrintWriter out;

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