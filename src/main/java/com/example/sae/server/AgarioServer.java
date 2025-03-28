package com.example.sae.server;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.*;
import com.example.sae.core.entity.powerUp.PowerUp;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class AgarioServer {
    private static final int PORT = 12345;
    private static final int TARGET_FPS = 30;
    private static final long FRAME_TIME = 1000000000 / TARGET_FPS; // 33ms en nanos

    private final GameEngine gameEngine;
    private final Map<String, ClientHandler> clientHandlers;
    private final ServerSocket serverSocket;
    private volatile boolean running;
    private Group root = new Group();

    public AgarioServer() throws IOException {
        this.gameEngine = new GameEngine(2000, 2000, true, true); // Même taille que le jeu original
        this.clientHandlers = new ConcurrentHashMap<>();
        this.serverSocket = new ServerSocket(PORT);
        this.running = true;
        startChatServer();

        initializeWorld();

    }

    private void startChatServer(){
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

    private void initializeWorld() {
        EntityFactory.setRoot(root);
        while (gameEngine.getEntitiesOfType(Food.class).size() < GameEngine.NB_FOOD_MAX) {
            gameEngine.addEntity(EntityFactory.createFood(GameEngine.MASSE_INIT_FOOD));
        }
        while (gameEngine.getEntitiesOfType(PowerUp.class).size() < GameEngine.NB_POWERUP_MAX) {
            gameEngine.addEntity(EntityFactory.createRandomPowerUp());
        }
    }

    public void start() {
        Thread gameUpdateThread = new Thread(this::gameLoop);
        gameUpdateThread.start();

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
        long lastFoodPowerUpUpdate = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            if (now - lastUpdate >= FRAME_TIME) {
                gameEngine.update();
                synchronizeEntities();
                lastUpdate = now;
            }

            if (now - lastFoodPowerUpUpdate >= 5000000000L) {
                lastFoodPowerUpUpdate = now;

                if (gameEngine.getEntitiesOfType(Food.class).size() < GameEngine.NB_FOOD_MAX) {
                    System.out.println("Adding food");
                    gameEngine.addEntity(EntityFactory.createFood(GameEngine.MASSE_INIT_FOOD));
                    gameEngine.getEntitiesOfType(Food.class).size();
                }

                if (gameEngine.getEntitiesOfType(Food.class).size() < GameEngine.NB_POWERUP_MAX) {
                    System.out.println("Adding powerup");
                    gameEngine.addEntity(EntityFactory.createRandomPowerUp());
                }

                System.out.println("---------------");
                clientHandlers.values().forEach(handler -> System.out.println(serializeEntity(handler.player)));
            }
        }
    }

    private void synchronizeEntities() {
        for (Entity entity : gameEngine.getEntitiesToAdd()) {
            if (!(entity instanceof MoveableBody)) {
                broadcastEntityCreation(entity);
            }
        }

        boolean broadcastedMasse = false;
        for (Entity entity : gameEngine.getEntitiesToRemove()) {
            broadcastEntityDeletion(entity);

            if (!broadcastedMasse) {
                for (Player player : gameEngine.getPlayers().values()) {
                    broadcastEntityMasse(player);
                }

                broadcastedMasse = true;
            }
        }

        gameEngine.cleanupEntities();
    }

    private void broadcastEntityCreation(Entity entity) {
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
        if (entity == null) return;
        clientHandlers.values().forEach(handler -> handler.sendMessage("DELETE|" + entity.getEntityId()));
    }

    private void broadcastEntityMasse(Player player) {
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
                entity.getColor().getGreen()*255,
                entity.getColor().getBlue()*255,
                entity instanceof MoveableBody ? ((MoveableBody) entity).getNom() :
                entity instanceof PowerUp ? ((PowerUp) entity).getType().name() : ":(");

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
                        System.out.println("Entity not found");
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