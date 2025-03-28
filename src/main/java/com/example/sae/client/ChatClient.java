package com.example.sae.client;

import javafx.application.Platform;
import com.example.sae.client.controller.SoloController;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Manages client-side chat communication with the server.
 */
public class ChatClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 55555;
    /// Handle the message send to the server
    private PrintWriter out;
    /// Handle the message send by the server
    private final Consumer<String> messageHandler;
    /// The player's name
    private final String playerName;

    /**
     * Constructor of the class
     * @param playerName The player's name
     * @param messageHandler
     */
    public ChatClient(String playerName, Consumer<String> messageHandler) {
        this.playerName = playerName;
        this.messageHandler = messageHandler;
    }

    /**
     * Starts the chat client, connects to the server, and listens for incoming messages in a background thread.
     */
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

    /**
     * Send a message to the server
     * @param message Message to send.
     */
    public void sendMessage(String message) {
        if (out != null) {
            String formattedMessage = playerName + "|" + message;
            out.println(formattedMessage);
        }
    }

}