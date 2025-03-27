package com.example.sae.server;

/**
 *
 */
public class ChatServerLauncher {
    public static void main(String[] args) {
        System.out.println("Starting chat server on port 55555...");
        ChatServer chatServer = new ChatServer();
        chatServer.start();
        System.out.println("Chat server is running. Press Ctrl+C to stop.");
    }
}