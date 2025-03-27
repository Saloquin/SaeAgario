package com.example.sae.client.utils;

import javafx.scene.paint.Color;
import java.io.*;
import java.util.Properties;

public class PreferencesManager {
    private static final String CONFIG_FILE = "player_prefs.ini";

    public static void savePreferences(String name, Color color) {
        Properties props = loadProperties();
        props.setProperty("player.name", name);
        props.setProperty("player.color", color.toString());
        saveProperties(props);
    }

    public static String[] loadPreferences() {
        Properties props = loadProperties();
        String playerName = props.getProperty("player.name");
        String playerColor = props.getProperty("player.color");
        if (playerName == null || playerColor == null) {
            return null;
        }
        return new String[] { playerName, playerColor };
    }

    public static Color parseColor(String colorStr) {
        try {
            return Color.valueOf(colorStr);
        } catch (Exception e) {
            return Color.BLUE; // Valeur par défaut
        }
    }

    // Nouveaux méthodes pour gérer l'IP du serveur

    public static String loadServerIP() {
        Properties props = loadProperties();
        return props.getProperty("server.ip", "");
    }

    public static void saveServerIP(String ip) {
        Properties props = loadProperties();
        props.setProperty("server.ip", ip);
        saveProperties(props);
    }

    // Méthode utilitaire pour charger les propriétés
    private static Properties loadProperties() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                props.load(input);
            } catch (IOException e) {
                System.err.println("Failed to load preferences: " + e.getMessage());
            }
        }
        return props;
    }

    // Méthode utilitaire pour sauvegarder les propriétés
    private static void saveProperties(Properties props) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "Player Preferences");
        } catch (IOException e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }
}
