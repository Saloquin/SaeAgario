package com.example.sae.server;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.*;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class AgarioServer {
    // normalement c bon
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
        for (int i = 0; i < 90; i++) {
            Food food = new Food(null, 2); // null car pas besoin de Group côté serveur
            gameEngine.addEntity(food);
        }
        gameEngine.addEntity(new Food(null, "a", 100, 0, 2, Color.BLACK));
        gameEngine.addEntity(new Food(null, "b", 100, 10, 2, Color.RED));
        gameEngine.addEntity(new Food(null, "c", 200, 0, 2, Color.BLACK));
        gameEngine.addEntity(new Food(null, "d", 200, 10, 2, Color.RED));
        gameEngine.addEntity(new Food(null, "e", 300, 0, 2, Color.BLACK));
        gameEngine.addEntity(new Food(null, "f", 300, 10, 2, Color.RED));
        gameEngine.addEntity(new Food(null, "g", 400, 0, 2, Color.BLACK));
        gameEngine.addEntity(new Food(null, "h", 400, 10, 2, Color.RED));
        gameEngine.addEntity(new Food(null, "i", 500, 0, 2, Color.BLACK));
        gameEngine.addEntity(new Food(null, "j", 500, 10, 2, Color.RED));
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
                gameEngine.update();
                synchronizeEntities();
                lastUpdate = now;
            }

            // à retirer
            if (now - groscaca >= 5000000000L) {
                groscaca = now;

                System.out.println("---------------");
                clientHandlers.values().forEach(handler -> System.out.println(serializeEntity(handler.player)));
            }
        }
    }

    private void synchronizeEntities() {
        // Synchronisation des entités entre le serveur et les clients
        for (Entity entity : gameEngine.getEntitiesToAdd()) {
            if (!(entity instanceof MoveableBody)) {
                broadcastEntityCreation(entity);
            }
        }

        boolean broadcastedMasse = false;
        for (Entity entity : gameEngine.getEntitiesToRemove()) {
            if (!(entity instanceof MoveableBody)) {
                broadcastEntityDeletion(entity);
            }

            if (!broadcastedMasse) {
                for (Player player : gameEngine.getPlayers().values()) {
                    broadcastEntityMasse(player);
                }

                broadcastedMasse = true;
            }
        }

        // Nettoyage manuel des entités
        gameEngine.cleanupEntities();
    }

    private void broadcastEntityCreation(Entity entity) {
        // pour éviter que les joueurs soient créés en double
        if (entity instanceof MoveableBody) {
            return;
        }

        // Sérialisation de l'entité
        String entityData = serializeEntity(entity);
        // Envoyer l'entité à tous les clients
        clientHandlers.values().forEach(handler -> handler.sendMessage("CREATE|" + entityData));
    }

    private void broadcastGameState() {
        String gameState = serializeGameState();
        clientHandlers.values().stream()
                .filter(ClientHandler::isReady)
                .forEach(handler -> handler.sendMessage(gameState));
    }

    private void broadcastEntityDeletion(Entity entity) {
        // Supprimer l'entité de tous les clients
        if (entity == null) return;
        // System.out.println("Delete prey broadcast : " + entity.getEntityId());
        clientHandlers.values().forEach(handler -> handler.sendMessage("DELETE|" + entity.getEntityId()));
    }

    private void broadcastEntityMasse(Player player) {
        // System.out.println("update mass broadcast: " +  player.getEntityId());
        clientHandlers.values().forEach(handler -> handler.sendMessage("UPDATEMASSE|" + player.getEntityId() + "|" + player.getMasse()));
    }


    private String serializeEntity(Entity entity) {
        return String.format(Locale.US, "%s,%s,%.2f,%.2f,%.2f,%.0f,%.0f,%.0f,%s|",
                entity.getClass().getSimpleName(),
                entity.getEntityId(),
                entity.getPosition()[0],
                entity.getPosition()[1],
                entity.getMasse(),
                entity.getColor().getRed()*255,
                entity.getColor().getBlue()*255,
                entity.getColor().getGreen()*255,
                entity instanceof MoveableBody ? ((MoveableBody) entity).getNom() : ":(");
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
            // this.player = new Player(null, clientId, MoveableBody.DEFAULT_MASSE, Color.RED); // null car pas besoin de Group côté serveur
            this.player = EntityFactory.createPlayer(clientId, MoveableBody.DEFAULT_MASSE, "verisubbo", Color.GREEN);
            this.player.setInputPosition(new double[]{ 0, 0 });
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
                    this.player.setNom(parts[1]);
                    int r = Integer.parseInt(parts[2]);
                    int g = Integer.parseInt(parts[3]);
                    int b = Integer.parseInt(parts[4]);
                    Color playerColor = Color.rgb(r, g, b);
                    this.player.getSprite().setFill(playerColor);
                    String gameState = serializeGameState();
                    sendMessage(gameState);

                    clientHandlers.values().forEach(handler -> handler.sendMessage("CREATE|" + serializeEntity(player)));
                }
                case "MOVE" -> {
                    if (parts.length == 3) {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);

                        player.getSprite().setCenterX(x);
                        player.getSprite().setCenterY(y);

                        clientHandlers.values().forEach(handler -> handler.sendMessage(String.format(Locale.US, "MOVE|%s|%f|%f", player.getEntityId(), x, y)));
                    }
                }
                case "DELETE" -> {
                    System.out.println("Delete prey serveur : " + parts[1]);
                    Entity entity = gameEngine.getEntityById(parts[1]);
                    if (entity == null) {
                        // System.out.println("Entity not found");
                        return;
                    }

                    gameEngine.removePrey(entity);
                    broadcastEntityDeletion(gameEngine.getEntityById(parts[1]));

                    Player player = (Player) gameEngine.getEntityById(parts[2]);
                    if (player == null) {
                        // System.out.println("Player not found");
                        return;
                    }

                    player.setMasse(Double.parseDouble(parts[3]));
                    broadcastEntityMasse(player);
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
                gameEngine.removeEntity(player);
                clientHandlers.remove(clientId);
                socket.close();

                clientHandlers.values().forEach(handler -> handler.sendMessage("DELETE|" + clientId));
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