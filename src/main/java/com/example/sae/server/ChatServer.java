package com.example.sae.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private static final int PORT = 55555;
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private final List<ChatMessage> messageHistory = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_MESSAGES = 10;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    static class ChatMessage implements Comparable<ChatMessage> {
        final LocalDateTime timestamp;
        final String playerName;
        final String content;

        ChatMessage(String playerName, String content) {
            this.timestamp = LocalDateTime.now();
            this.playerName = playerName;
            this.content = content;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s: %s",
                    timestamp.format(formatter), playerName, content);
        }

        @Override
        public int compareTo(ChatMessage other) {
            return other.timestamp.compareTo(this.timestamp); // Pour trier du plus récent au plus ancien
        }
    }

    public void start() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final PrintWriter out;
        private final BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Envoyer l'historique au nouveau client
            synchronized (messageHistory) {
                List<ChatMessage> sortedMessages = new ArrayList<>(messageHistory);
                Collections.sort(sortedMessages);
                for (ChatMessage msg : sortedMessages) {
                    out.println(msg.toString());
                }
            }
        }

        @Override
        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    String[] parts = input.split("\\|", 2);
                    if (parts.length == 2) {
                        String playerName = parts[0];
                        String message = parts[1];

                        ChatMessage chatMessage = new ChatMessage(playerName, message);

                        synchronized (messageHistory) {
                            if (messageHistory.size() >= MAX_MESSAGES) {
                                messageHistory.remove(0);
                            }
                            messageHistory.add(chatMessage);
                        }
                        broadcast(chatMessage.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(this);
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }

    private void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}