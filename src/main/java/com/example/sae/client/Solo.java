package com.example.sae.client;

import com.example.sae.client.utils.debug.DebugWindow;
import com.example.sae.client.utils.timer.GameTimer;
import com.example.sae.core.entity.EntityFactory;
import com.example.sae.core.entity.Food;
import com.example.sae.core.entity.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Solo extends Client {
    private GameTimer gameTimer;
    private Player player;

    private final BooleanProperty gameIsEnded = new SimpleBooleanProperty(false);

    public Solo(Group root, String playerName, Color color) {
        super(root, playerName, color);
        this.gameTimer = new GameTimer(this);
    }

    @Override
    public void init() {
        gameStarted = true;
        player = EntityFactory.createPlayer(10,  playerName,color);
        playerId = gameEngine.addPlayer(player);
        if(DebugWindow.DEBUG_MODE) {
            DebugWindow.getInstance();
        }

        while (gameEngine.getEntitiesOfType(Food.class).size() < 100) {
            gameEngine.addEntity(EntityFactory.createFood(4));
        }

        gameTimer.start();
    }

    @Override
    public void update() {
        Player player = gameEngine.getPlayer(playerId);
        if (player == null) {
            gameIsEnded.set(true);
        }
        else if (!gameIsEnded.get()) {
            player.setInputPosition(getMousePosition());
            manageEntities();

            gameEngine.update();
            if (DebugWindow.DEBUG_MODE && DebugWindow.getInstance().getController() != null) {
                DebugWindow.getInstance().update(gameEngine, playerId);
            }
        }
    }

    public BooleanProperty getGameIsEndedProperty() {
        return gameIsEnded;
    }

    public void stopSoloGame() {
        gameTimer.stop();
        if(!gameIsEnded.get()) {
            gameIsEnded.set(true);
        }
        gameEngine = null;
    }
}
