package com.example.sae.server;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.MoveableBody;
import com.example.sae.core.entity.Player;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class AgarioServer {
    // note : ne pas envoyer un gamestate par frame
    // envoyer simplement quand un élément est créé/modifié/supprimé avec les informations de cet élément
    private static final int PORT = 12345;
    private static final int TARGET_FPS = 30;
    private static final long FRAME_TIME = 1000000000 / TARGET_FPS; // 33ms en nanos

    private final GameEngine gameEngine;
    private final Map<String, ClientHandler> clientHandlers;
    private final ServerSocket serverSocket;
    private volatile boolean running;

    public AgarioServer() throws IOException {
        this.gameEngine = new GameEngine(2000, 2000, true); // Même taille que le jeu original
        this.clientHandlers = new ConcurrentHashMap<>();
        this.serverSocket = new ServerSocket(PORT);
        this.running = true;

        initializeWorld();
    }

    private void initializeWorld() {
        // Initialiser la nourriture
        for (int i = 0; i < 100; i++) {
            Food food = new Food(null, 2); // null car pas besoin de Group côté serveur
            gameEngine.addEntity(food);
        }
    }

    public void start() {
        // Thread pour la mise à jour du jeu
        Thread gameUpdateThread = new Thread(this::gameLoop);
        gameUpdateThread.start();

        // Thread principal pour accepter les connexions
        System.out.println("Serveur démarré sur le port " + PORT);
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                String clientId = UUID.randomUUID().toString();
                ClientHandler handler = new ClientHandler(clientSocket, clientId);
                clientHandlers.put(clientId, handler);
                new Thread(handler).start();
            } catch (IOException e) {
                if (running) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void gameLoop() {
        long lastUpdate = System.nanoTime();
        long groscaca = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            if (now - lastUpdate >= FRAME_TIME) {
                // gameEngine.update();
                // broadcastGameState();
                lastUpdate = now;
            }

            // à retirer
            if (now - groscaca >= 5000000000L) {
                // System.out.println(serializeGameState());
                clientHandlers.values().stream().findFirst().ifPresent(clientHandler -> {
                    // System.out.println(serializeEntity(clientHandler.player));
                });
                groscaca = now;
                StringBuilder state = new StringBuilder("DELETE|");

                for (Entity entity : gameEngine.getEntitiesOfType(Food.class)) {
                    state.append(entity.getEntityId()).append("|");
                }

                // clientHandlers.values().forEach(handler -> handler.sendMessage(state.toString()));
            }
        }
    }

    private void broadcastGameState() {
        String gameState = serializeGameState();
        clientHandlers.values().stream()
                .filter(ClientHandler::isReady)
                .forEach(handler -> handler.sendMessage(gameState));
    }

    private String serializeEntity(Entity entity) {
        return String.format(Locale.US, "%s,%s,%.2f,%.2f,%.2f,%.0f,%.0f,%.0f|",
                entity.getClass().getSimpleName(),
                entity.getEntityId(),
                entity.getPosition()[0],
                entity.getPosition()[1],
                entity.getMasse(),
                entity.getColor().getRed()*255,
                entity.getColor().getBlue()*255,
                entity.getColor().getGreen()*255);
    }

    private String serializeGameState() {
        StringBuilder state = new StringBuilder("GAMESTATE|");

        for (Entity entity : gameEngine.getEntities()) {
            state.append(serializeEntity(entity));
        }

        return state.toString();
    }

    class ClientHandler implements Runnable {
        private final Socket socket;
        private final String clientId;
        private final PrintWriter out;
        private final BufferedReader in;
        private boolean ready;
        private Player player;

        public ClientHandler(Socket socket, String clientId) throws IOException {
            this.socket = socket;
            this.clientId = clientId;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.ready = false;

            // Créer un nouveau joueur pour ce client
            this.player = new Player(null, clientId, MoveableBody.DEFAULT_MASSE, Color.RED); // null car pas besoin de Group côté serveur
            gameEngine.addPlayer(player);

            // Envoyer l'ID et les informations initiales
            sendMessage("ID|" + clientId);
        }

        @Override
        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    handleClientInput(input);
                }
            } catch (IOException e) {
                // e.printStackTrace();
            } finally {
                disconnect();
            }
        }

        private void handleClientInput(String input) {
            // System.out.println("Server received: " + input);
            String[] parts = input.split("\\|");
            if (parts.length < 1) return;

            switch (parts[0]) {
                case "READY" -> {
                    ready = true;
                    String gameState = serializeGameState();
                    sendMessage(gameState);

                    clientHandlers.values().forEach(handler -> handler.sendMessage("CREATE|" + serializeEntity(player)));
                }
                case "MOVE" -> {
                    if (parts.length == 3) {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        player.moveToward(new double[]{ x, y });

                        clientHandlers.values().forEach(handler -> handler.sendMessage(String.format(Locale.US, "MOVE|%s|%f|%f", player.getEntityId(), x, y)));
                    }
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        public boolean isReady() {
            return ready;
        }

        private void disconnect() {
            try {
                System.out.println("LOG TEMPORAIRE : client déconnecté");
                gameEngine.removeEntity(player);
                clientHandlers.remove(clientId);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new AgarioServer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}