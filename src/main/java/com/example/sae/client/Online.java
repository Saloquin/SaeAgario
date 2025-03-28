package com.example.sae.client;

import com.example.sae.client.utils.timer.GameTimer;
import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.immobile.Food;
import com.example.sae.core.entity.movable.Player;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static com.example.sae.client.controller.SoloController.getMousePosition;

/**
 * Manages the game logic when the player is playing online.
 */
public class Online extends Client {

    /// The unique client ID assigned by the server
    private String clientId;

    /// Timer for game updates
    private final GameTimer gameTimer;

    /// Background thread handling the server
    private final ThreadDeFond handler;

    /// The socket used to communicate with the server
    private final Socket socket;

    /**
     * Constructs an online client for multiplayer gameplay.
     *
     * @param root The root group containing the game elements
     * @param playerName The name of the player
     * @param color The color of the player's sprite
     * @throws IOException If the connection to the server fails
     */
    public Online(Group root, String playerName, Color color) throws IOException {
        super(root, playerName, color);
        this.gameTimer = new GameTimer(this);
        this.gameEngine = new GameEngine(false);
        this.socket = new Socket("localhost", 12345);
        handler = new ThreadDeFond(this, socket);
        new Thread(handler).start();
        handler.sendMessage("READY");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        gameStarted = true;
        Player player = EntityFactory.createPlayer(3, "Player", Color.RED);
        camera.focusOn(player);
        gameEngine.addPlayer(player);
        gameTimer.start();
    }

    /**
     * {@inheritDoc}
     */
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

        handler.sendMessage(String.format(Locale.US, "MOVE|%f|%f", getMousePosition()[0], getMousePosition()[1]));
        gameEngine.update();
    }

    /**
     * Unused
     */
    public void handleAppClosed(Stage stage) {
        stage.setOnHiding(event -> {
            try {
                socket.close();
            } catch (IOException e) {
                // Silently fail
            }
        });
    }

    /**
     * Handles incoming messages and updates from the server in a separate thread.
     */
    class ThreadDeFond implements Runnable {

        private final Online client;
        private final PrintWriter out;
        private final BufferedReader in;

        /**
         * Constructs the handler for server communication.
         *
         * @param client The associated online client
         * @param socket The socket for server communication
         * @throws IOException If input/output streams cannot be initialized
         */
        public ThreadDeFond(Online client, Socket socket) throws IOException {
            this.client = client;
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        /**
         * Listens for messages from the server and handles them.
         */
        @Override
        public void run() {
            try {
                String input;
                while ((input = in.readLine()) != null) {
                    handleClientInput(input);
                }
            } catch (IOException e) {
                // Silent catch, could log if needed
            }
        }

        /**
         * Parses and processes server commands.
         *
         * @param input The full message string received from the server
         */
        private void handleClientInput(String input) {
            String[] parts = input.split("\\|");
            if (parts.length < 1) return;

            switch (parts[0]) {
                case "ID" -> clientId = parts[1];
                case "GAMESTATE" -> setupWithSocketData(Arrays.copyOfRange(parts, 1, parts.length));
                case "CREATE" -> {}  // Future use
                case "UPDATE" -> {}  // Future use
                case "DELETE" -> deleteWithSocketData(Arrays.copyOfRange(parts, 1, parts.length));
            }
        }

        /**
         * Initializes game entities based on server data.
         *
         * @param parts Array of encoded entity data
         */
        public void setupWithSocketData(String[] parts) {
            for (String part : parts) {
                String[] infos = part.split(",");

                switch (infos[0]) {
                    case "Player" -> {
                        double x = Double.parseDouble(infos[2]);
                        double y = Double.parseDouble(infos[3]);
                        String id = infos[1];
                        if (!id.equals(clientId)) {
                            Platform.runLater(() -> {
                                Player player = new Player(root, id, x, y, 5, Color.BLUE, false);
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

            Platform.runLater(() -> gameEngine.update());
        }

        /**
         * Unused
         */
        public void updateWithSocketData(String[] parts) {
            for (String part : parts) {
                String[] infos = part.split(",");

                switch (infos[0]) {
                    case "Player" -> {
                        double x = Double.parseDouble(infos[2]);
                        double y = Double.parseDouble(infos[3]);
                        Platform.runLater(() -> {
                            Player player = new Player(root, infos[1], x, y, 5, Color.RED, false);
                            camera.focusOn(player);
                            gameEngine.addPlayer(player);
                        });
                    }
                    case "Food" -> {
                        double x = Double.parseDouble(infos[2]);
                        double y = Double.parseDouble(infos[3]);
                        double masse = Double.parseDouble(infos[4]);
                        int r = Integer.parseInt(infos[5]);
                        int g = Integer.parseInt(infos[6]);
                        int b = Integer.parseInt(infos[7]);
                        Platform.runLater(() -> {
                            gameEngine.addEntity(new Food(root, "1", x, y, masse, Color.rgb(r, g, b, 0.99)));
                        });
                    }
                    default -> System.out.println(part);
                }
            }

            Platform.runLater(() -> gameEngine.update());
        }

        /**
         * Removes entities from the game based on IDs provided by the server.
         *
         * @param entitiesId The list of entity IDs to remove
         */
        public void deleteWithSocketData(String[] entitiesId) {
            Stream<Entity> allEntities = gameEngine.getEntities().stream();
            List<Entity> entitiesToRemove = allEntities
                    .filter(e -> Arrays.asList(entitiesId).contains(e.getEntityId()))
                    .toList();

            entitiesToRemove.forEach(entity ->
                    Platform.runLater(() -> {
                        gameEngine.removeEntity(entity);
                        entity.onDeletion();
                    })
            );
        }

        /**
         * Sends a message to the server.
         *
         * @param message The message string to send
         */
        public void sendMessage(String message) {
            out.println(message);
        }
    }

    @Override
    public void stopGame(){
        
    }

    /**
     * Exits the application and returns to the menu.
     */
    private void returnToMenu() {
        Platform.exit();
    }
}
