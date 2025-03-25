package com.example.sae.client;

import com.example.sae.core.GameEngine;
import com.example.sae.core.entity.Entity;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import javafx.animation.AnimationTimer;
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
import java.util.Locale;

import static com.example.sae.core.GameEngine.MAP_LIMIT_HEIGHT;
import static com.example.sae.core.GameEngine.MAP_LIMIT_WIDTH;

public class Online extends Client{
    private String clientId;
    private Online.GameTimer gameTimer;
    private ThreadDeFond handler;
    private final Socket socket;

    public Online(Group root) throws IOException {
        super(root);
        this.gameTimer = new Online.GameTimer(this);
        this.gameEngine = new GameEngine(MAP_LIMIT_WIDTH, MAP_LIMIT_HEIGHT, false);
        this.socket = new Socket("localhost", 12345);
        handler = new ThreadDeFond(this, socket);
        new Thread(handler).start();
        handler.sendMessage("READY");
    }

    @Override
    public void init() {
        gameStarted = true;
        Player player = new Player(root, 5, Color.RED, false); // Joueur distant
        player.setCamera(camera);
        camera.focusOn(player);
        gameEngine.addPlayer(player);
        gameTimer.start();
    }

    @Override
    public void update() {
        // gameEngine.getPlayer(playerId).setInputPosition(getMousePosition());
        handler.sendMessage(String.format(Locale.US, "MOVE|%f|%f", getMousePosition()[0], getMousePosition()[1]));

        gameEngine.update();
    }

    public void updateWithSocketData(String[] parts) {
        for (Entity entity : gameEngine.getEntities()) {
            // retirer cette condition pour rebuild le joueur et les ennemis à chaque fois
            // if (entity instanceof Food) {
                Platform.runLater(() -> {
                    gameEngine.removeEntity(entity);
                    entity.onDeletion();
                });
            // }
        }

        // Player,null,0,00,0,00,5,00
        // Food,food,-1614.77,-610.76,2.00
        for (String part : parts) {
            String[] infos = part.split(",");

            switch (infos[0]) {
                case "Player" -> {
                    double x = Double.parseDouble(infos[2]);
                    double y = Double.parseDouble(infos[3]);
                    Platform.runLater(() -> {
                        Player player = new Player(root, x, y, 5, Color.RED, false);
                        player.setCamera(camera);
                        camera.focusOn(player);
                        gameEngine.addPlayer(player);
                    });
                    // System.out.println(part);
                }
                case "Food" -> {
                    double x = Double.parseDouble(infos[2]);
                    double y = Double.parseDouble(infos[3]);
                    double masse = Double.parseDouble(infos[4]);
                    int r = Integer.parseInt(infos[5]);
                    int g = Integer.parseInt(infos[6]);
                    int b = Integer.parseInt(infos[7]);
                    Platform.runLater(() -> {
                        gameEngine.addEntity(new Food(root, x, y, masse, Color.rgb(r, g, b, 0.99)));
                    });
                }
                default -> System.out.println(part);
            }
        }

        Platform.runLater(() -> {
            gameEngine.update();
        });
    }

    public void handleAppClosed(Stage stage) {
        stage.setOnHiding(event -> {
            try {
                socket.close();
            } catch (IOException e) {
                // throw new RuntimeException(e);
            }
        });
    }

    private static class GameTimer extends AnimationTimer {
        private final Online client;
        private final double framesPerSecond = 120;
        private final double interval = 1000000000 / framesPerSecond;
        private double last = 0;

        public GameTimer(Online client) {
            this.client = client;
        }

        @Override
        public void handle(long now) {
            if (last == 0) {
                last = now;
            }

            if (now - last > interval && client.gameStarted) {
                last = now;
                // envoyer déplacements au serveur
                client.update();
            }
        }
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
                case "ID" -> clientId = parts[1];
                case "GAMESTATE" -> {
                    client.updateWithSocketData(Arrays.copyOfRange(parts, 1, parts.length));
                    // System.out.println(input);
                    // gameEngine.addEntity(new Food(root, 2));
                    // gameEngine.addEntity(new Enemy(root, 5));
                }
            }
        }

        public void sendMessage(String message) {
            out.println(message);
        }
    }
}
