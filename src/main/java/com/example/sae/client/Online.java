package com.example.sae.client;

import com.example.sae.client.utils.debug.DebugWindow;
import com.example.sae.client.utils.timer.GameTimer;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.stream.Stream;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Online extends Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;
    private String clientId;
    private final GameTimer gameTimer;
    private final ThreadDeFond handler;
    private final Socket socket;
    private Player player;

    private final BooleanProperty gameIsEnded = new SimpleBooleanProperty(false);

    public Online(Group root, String playerName, Color color) throws IOException {
        super(root, playerName, color);
        EntityFactory.setRoot(root);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, true);
        this.socket = new Socket(HOST, PORT);
        handler = new ThreadDeFond(this, socket);
        new Thread(handler).start();
    }

    @Override
    public void init() {
        gameStarted = true;
        player = EntityFactory.createPlayer(MoveableBody.DEFAULT_MASSE, playerName, color);
        playerId = gameEngine.addPlayer(player);
        gameTimer.start();
        if (DebugWindow.DEBUG_MODE) {
            DebugWindow.getInstance();
        }
        handler.sendMessage(String.format(Locale.US, "READY|%s|%.0f|%.0f|%.0f", playerName, color.getRed()*255, color.getBlue()*255, color.getGreen()*255));
    }

    @Override
    public void update() {
        Player player = gameEngine.getPlayer(playerId);
        if (player == null) {
            gameIsEnded.set(true);
            return;
        }

        player.setInputPosition(getMousePosition());
        handler.sendMessage(String.format(Locale.US, "MOVE|%.2f|%.2f", player.getPosition()[0], player.getPosition()[1]));

        gameEngine.update();

        // if eat entity in client side, send to server
        for (Entity entity : gameEngine.getEntitiesToRemove()) {
            // System.out.println("Delete prey local : " + entity.getEntityId());
            removePrey(entity);
        }

        gameEngine.cleanupEntities();

        if (DebugWindow.DEBUG_MODE && DebugWindow.getInstance().getController() != null) {
            DebugWindow.getInstance().update(gameEngine, playerId);
        }
    }


    private void removePrey(Entity prey) {
        if (prey == null) return;
        handler.sendMessage("DELETE|" + prey.getEntityId());
        // System.out.println("update mass local: " +  player.getEntityId());
        handler.sendMessage("UPDATEMASSE|" + player.getEntityId() + "|" + player.getMasse());
    }

    class ThreadDeFond implements Runnable {
        private final Online client;
        private final PrintWriter out;
        private final BufferedReader in;

        public ThreadDeFond(Online client, Socket socket) throws IOException {
            this.client = client;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            }
        }

        private void handleClientInput(String input) {
            String[] parts = input.split("\\|");
            if (parts.length < 1) return;

            switch (parts[0]) {
                case "ID" -> {
                    clientId = parts[1];
                    Platform.runLater(() -> {
                        player.setEntityId(clientId);
                    });
                }
                case "GAMESTATE", "CREATE" -> createEntityUsingSocketData(Arrays.copyOfRange(parts, 1, parts.length));
                case "MOVE" -> movePlayerUsingSocketData(parts);
                case "DELETE" -> {
                    // System.out.println(input);
                    // DELETE|123456 => 123456
                    deleteEntityUsingSocketData(parts[1]);
                }
                case "UPDATEMASSE" -> {
                    String id = parts[1];
                    // System.out.println("Update masse local broadcast: " + id);
                    double masse = Double.parseDouble(parts[2]);
                    Player player = (Player)gameEngine.getEntityById(id);
                    // System.out.println("player: " + player);
                    if (player != null) {
                        System.out.println("nouvelle masse de " + player.getNom() + " : " + masse);
                        player.setMasse(masse);
                    } else {
                        System.out.println("le gros nul");
                    }
                }
            }
        }

        public void createEntityUsingSocketData(String[] parts) {
            for (String part : parts) {
                String[] infos = part.split(",");

                switch (infos[0]) {
                    case "Player" -> {
                        String id = infos[1];
                        double x = Double.parseDouble(infos[2]);
                        double y = Double.parseDouble(infos[3]);
                        double masse = Double.parseDouble(infos[4]);
                        int r = Integer.parseInt(infos[5]);
                        int g = Integer.parseInt(infos[6]);
                        int b = Integer.parseInt(infos[7]);
                        String playerName = infos[8];
                        if (!id.equals(clientId)) {
                            Platform.runLater(() -> {
                                // System.out.println(playerName);
                                Player player = EntityFactory.createPlayer(id, x, y, masse, playerName, Color.rgb(r, g, b));
                                gameEngine.addPlayer(player);
                                player.setInputPosition(new double[]{ x, y });
                                player.moveToward(new double[]{ x, y });
                            });
                        }
                    }
                    case "Food" -> {
                        double x = Double.parseDouble(infos[2]);
                        double y = Double.parseDouble(infos[3]);
                        double masse = Double.parseDouble(infos[4]);
                        int r = Integer.parseInt(infos[5]);
                        int g = Integer.parseInt(infos[6]);
                        int b = Integer.parseInt(infos[7]);
                        Platform.runLater(() -> {
                            gameEngine.addEntity(new Food(root, infos[1], x, y, masse, Color.rgb(r, g, b, 0.99)));
                        });
                    }
                    default -> System.out.println(part);
                }
            }
        }

        // pour move un autre gars, pas nous
        public void movePlayerUsingSocketData(String[] parts) {
            String movingPlayerId = parts[1];

            if (movingPlayerId.equals(clientId)) {
                return;
                // System.out.println(clientId);
            }

            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);

            // Stream<Entity> allPlayers = gameEngine.getEntitiesOfType(Player.class).stream();
            Stream<MoveableBody> allPlayers = gameEngine.entitiesMovable.stream();
            List<MoveableBody> movingPlayers = allPlayers.filter(p -> movingPlayerId.equals(p.getEntityId())).toList();

            movingPlayers.forEach(entity -> {
                Platform.runLater(() -> {
                    /*
                    if (!movingPlayerId.equals(clientId)) {
                        System.out.println(clientId + " /// " + movingPlayerId + " /// " + entity.getEntityId());
                        System.out.println(x + ", " + y);
                    }
                    */
                    Player movingPlayer = (Player)entity;
                    movingPlayer.setInputPosition(new double[]{ x, y });
                    movingPlayer.moveToward(new double[]{ x, y });
                    // movingPlayer.getSprite().setCenterX(x);
                    // movingPlayer.getSprite().setCenterY(y);
                });
            });
        }

        public void deleteEntityUsingSocketData(String entitiesId) {
            // System.out.println("Delete prey local broadcast: " + entitiesId);
            Entity entity = gameEngine.getEntityById(entitiesId);

            if (entity != null) {
                Platform.runLater(() -> {
                    gameEngine.removeEntity(entity);
                    entity.onDeletion();
                });
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }

    public BooleanProperty getGameIsEndedProperty() {
        return gameIsEnded;
    }

    public void stopOnlineGame() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (DebugWindow.DEBUG_MODE && DebugWindow.getInstance().getController() != null) {
            DebugWindow.getInstance().getController().getStage().close();
        }
        gameTimer.stop();
    }
}
