package com.example.sae.client.utils.config;

public final class Constants {
    private Constants() {}



    // Map settings
    public static double getMapLimitWidth() {
        return ConfigLoader.getDouble("Map", "MAP_LIMIT_WIDTH");
    }

    public static double getMapLimitHeight() {
        return ConfigLoader.getDouble("Map", "MAP_LIMIT_HEIGHT");
    }

    // Entity settings
    public static double getMinMasseSplit() {return ConfigLoader.getDouble("Entities", "MIN_MASSE_SPLIT");}
    public static double getCloneSplitDistance() {return ConfigLoader.getDouble("Entities", "ClONE_SPLIT_DISTANCE");}
    public static double getCloneMergeCooldown() {return ConfigLoader.getDouble("Entities", "CLONE_MERGE_COOLDOWN");}

    public static double getNbFoodMax() {
        return ConfigLoader.getDouble("Entities", "NB_FOOD_MAX");
    }

    public static double getNbPowerUpMax() {
        return ConfigLoader.getDouble("Entities", "NB_POWERUP_MAX");
    }

    public static double getNbEnemyMax() {
        return ConfigLoader.getDouble("Entities", "NB_ENEMY_MAX");
    }

    public static double getMasseInitPlayer() {
        return ConfigLoader.getDouble("Entities", "MASSE_INIT_PLAYER");
    }

    public static double getMasseInitFood() {
        return ConfigLoader.getDouble("Entities", "MASSE_INIT_FOOD");
    }

    public static double getMasseInitEnemy() {
        return ConfigLoader.getDouble("Entities", "MASSE_INIT_ENEMY");
    }

    public static int getEnemyRange() {
        return ConfigLoader.getInt("Entities", "ENEMY_RANGE");
    }

    // Movement settings
    public static double getBaseMaxSpeed() {
        return ConfigLoader.getDouble("Movement", "BASE_MAX_SPEED");
    }

    public static double getEnemySpeedMultiplier() {
        return ConfigLoader.getDouble("Movement", "ENEMY_SPEED_MULTIPLIER");
    }

    public static double getEatingSizeRatio() {
        return ConfigLoader.getDouble("Movement", "EATING_SIZE_RATIO");
    }

    // Camera settings
    public static double getCameraZoomFactor() {
        return ConfigLoader.getDouble("Camera", "CAMERA_ZOOM_FACTOR");
    }

    public static double getCameraDezoomFactor() {
        return ConfigLoader.getDouble("Camera", "CAMERA_DEZOOM_FACTOR");
    }

    // Network settings
    public static int getDefaultPort() {
        return ConfigLoader.getInt("Network", "DEFAULT_PORT");
    }

    // QuadTree settings
    public static int getQuadTreeMaxDepth() {
        return ConfigLoader.getInt("QuadTree", "QUAD_TREE_MAX_DEPTH");
    }
}
