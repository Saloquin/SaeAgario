package com.example.sae.client;

import com.example.sae.client.debug.DebugWindow;
import com.example.sae.client.timer.GameTimer;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.*;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
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

    public Online(Group root) throws IOException {
        super(root);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
        this.socket = new Socket(HOST, PORT);
        handler = new ThreadDeFond(this, socket);
        new Thread(handler).start();
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        Player player = gameEngine.getPlayer(playerId);

        if (player == null) {
            new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(
                            javafx.util.Duration.seconds(2),
                            event -> returnToMenu()
                    )
            ).play();
            return;
        }

        // player.setInputPosition(getMousePosition());
        handler.sendMessage(String.format(Locale.US, "MOVE|%.2f|%.2f", getMousePosition()[0], getMousePosition()[1]));

        gameEngine.update();

        /*
        if (DebugWindow.DEBUG_MODE && DebugWindow.getInstance().getController() != null) {
            DebugWindow.getInstance().update(gameEngine, playerId);
        }*/
    }

    public void start(String id) {
        gameStarted = true;
        Player player = EntityFactory.createPlayer(id, MoveableBody.DEFAULT_MASSE, Color.RED, true);
        player.setCamera(camera);
        camera.focusOn(player);
        gameEngine.addPlayer(player);
        gameTimer.start();
        if (DebugWindow.DEBUG_MODE) {
            // DebugWindow.getInstance();
        }
        handler.sendMessage("READY");
    }

    public void handleAppClosed(Stage stage) {
        stage.setOnHiding(event -> {
            try {
                socket.close();
                DebugWindow.getInstance().getController().getStage().close();
            } catch (IOException e) {
                // throw new RuntimeException(e);
            }
        });
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
                        start(clientId);
                    });
                }
                case "GAMESTATE", "CREATE" -> createEntityUsingSocketData(Arrays.copyOfRange(parts, 1, parts.length));
                case "MOVE" -> movePlayerUsingSocketData(parts);
                case "DELETE" -> {
                    // System.out.println(input);
                    deleteEntityUsingSocketData(Arrays.copyOfRange(parts, 1, parts.length));
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
                        if (!id.equals(clientId)) {
                            // System.out.println(part);
                            Platform.runLater(() -> {
                                Player player = new Player(root, id, x, y, masse, Color.BLUE, false);
                                gameEngine.addPlayer(player);
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

            /*
            if (movingPlayerId.equals(clientId)) {
                // return;
                // System.out.println(clientId);
            }
            */

            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);

            Stream<Entity> allPlayers = gameEngine.getEntitiesOfType(Player.class).stream();
            List<Entity> movingPlayers = allPlayers.filter(p -> movingPlayerId.equals(p.getEntityId())).toList();

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
                });
            });

            /*
            Stream<Entity> allPd = gameEngine.getEntitiesOfType(Player.class).stream().filter(p -> movingPlayerId.equals(p.getEntityId()));

            if (!movingPlayerId.equals(clientId)) {
                // System.out.println(movingPlayerId + " /// " + allPlayers.findFirst().get().getEntityId());
                System.out.println(movingPlayerId + " /// " + allPd.findFirst().get().getEntityId());
                System.out.println(x + ", " + y);
            }

            /*
            List<Entity> movingPlayers = allPlayers.filter(p -> movingPlayerId.equals(p.getEntityId())).toList();
            Player movingPlayer = (Player)movingPlayers.get(0);

            Platform.runLater(() -> {
                movingPlayer.setInputPosition(new double[]{ x, y });
            });

            allPlayers.filter(p -> movingPlayerId.equals(p.getEntityId())).findFirst().ifPresent(entity -> {
                Platform.runLater(() -> {
                    Player movingPlayer = (Player)entity;
                    movingPlayer.setInputPosition(new double[]{ x, y });
                });
            });
            */
        }

        public void deleteEntityUsingSocketData(String[] entitiesId) {
            Stream<Entity> allEntities = gameEngine.getEntities().stream();
            List<Entity> entitiesToRemove = allEntities.filter(e -> Arrays.asList(entitiesId).contains(e.getEntityId())).toList();

            entitiesToRemove.forEach(entity -> {
                Platform.runLater(() -> {
                    gameEngine.removeEntity(entity);
                    entity.onDeletion();
                });
            });
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }

    private void returnToMenu() {
        Platform.exit();
    }
}
