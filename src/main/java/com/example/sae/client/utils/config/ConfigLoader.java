package com.example.sae.client.utils.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    private static final String CONFIG_FILE = "constants.ini";
    private static final Map<String, Map<String, String>> sections = new HashMap<>();
    private static boolean isLoaded = false;

    private ConfigLoader() {}

    public static void loadConfig() {
        if (isLoaded) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            String line;
            String currentSection = null;
            Map<String, String> currentMap = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith("[") && line.endsWith("]")) {
                    currentSection = line.substring(1, line.length() - 1);
                    currentMap = new HashMap<>();
                    sections.put(currentSection, currentMap);
                } else if (currentMap != null && line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    currentMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            isLoaded = true;
        } catch (IOException e) {
            throw new RuntimeException("Could not load " + CONFIG_FILE, e);
        }
    }

    public static double getDouble(String section, String key) {
        String value = getValue(section, key);
        return Double.parseDouble(value);
    }

    public static int getInt(String section, String key) {
        String value = getValue(section, key);
        return Integer.parseInt(value);
    }

    public static String getString(String section, String key) {
        return getValue(section, key);
    }

    private static String getValue(String section, String key) {
        if (!isLoaded) {
            loadConfig();
        }

        Map<String, String> sectionMap = sections.get(section);
        if (sectionMap == null) {
            throw new IllegalArgumentException("Section not found: " + section);
        }

        String value = sectionMap.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Key not found: " + key + " in section " + section);
        }

        return value;
    }
}
