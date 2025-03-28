module com.example.sae {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.accessibility;

    opens com.example.sae to javafx.fxml;
    exports com.example.sae.core.entity.movable.enemyStrategy;
    opens com.example.sae.core.entity.movable.enemyStrategy to javafx.fxml;
    exports com.example.sae.core.entity;
    opens com.example.sae.core.entity to javafx.fxml;
    opens com.example.sae.client to javafx.fxml;
    exports com.example.sae.core.quadtree;
    opens com.example.sae.core.quadtree to javafx.fxml;
    exports com.example.sae.core;
    opens com.example.sae.core to javafx.fxml;
    exports com.example.sae.client;
    exports com.example.sae.client.controller;
    opens com.example.sae.client.controller to javafx.fxml;
    exports com.example.sae.client.factory;
    opens com.example.sae.client.factory to javafx.fxml;
    exports com.example.sae.core.entity.immobile.powerUp;
    opens com.example.sae.core.entity.immobile.powerUp to javafx.fxml; // Add this line
    exports com.example.sae.client.utils.timer;
    opens com.example.sae.client.utils.timer to javafx.fxml;
    exports com.example.sae.client.utils.debug;
    opens com.example.sae.client.utils.debug to javafx.fxml;
    exports com.example.sae.core.entity.movable;
    opens com.example.sae.core.entity.movable to javafx.fxml;
    exports com.example.sae.core.entity.immobile;
    opens com.example.sae.core.entity.immobile to javafx.fxml;
    exports com.example.sae.core.entity.movable.body;
    opens com.example.sae.core.entity.movable.body to javafx.fxml; // Add this line
}