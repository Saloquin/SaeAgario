package com.example.sae.client.utils;

import javafx.scene.paint.Color;
import java.io.*;
import java.util.Properties;

public class PreferencesManager {
    private static final String CONFIG_FILE = "player_prefs.ini";

    public static void savePreferences(String name, Color color) {
        Properties props = new Properties();
        props.setProperty("player.name", name);
        props.setProperty("player.color", color.toString());

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.store(output, "Player Preferences");
        } catch (IOException e) {
            System.err.println("Failed to save preferences: " + e.getMessage());
        }
    }

    public static String[] loadPreferences() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE);

        if (!configFile.exists()) {
            return null;
        }

        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);
            return new String[] {
                    props.getProperty("player.name"),
                    props.getProperty("player.color")
            };
        } catch (IOException e) {
            System.err.println("Failed to load preferences: " + e.getMessage());
            return null;
        }
    }

    public static Color parseColor(String colorStr) {
        try {
            return Color.valueOf(colorStr);
        } catch (Exception e) {
            return Color.BLUE; // Valeur par défaut
        }
    }
}