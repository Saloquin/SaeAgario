module com.example.sae {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.accessibility;

    opens com.example.sae to javafx.fxml;
    exports com.example.sae.core.entity.enemyStrategy;
    opens com.example.sae.core.entity.enemyStrategy to javafx.fxml;
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
    exports com.example.sae.client.handler;
    opens com.example.sae.client.handler to javafx.fxml;
    exports com.example.sae.client.factory;
    opens com.example.sae.client.factory to javafx.fxml;
    exports com.example.sae.client.timer;
    opens com.example.sae.client.timer to javafx.fxml;
    exports com.example.sae.client.debug;
    opens com.example.sae.client.debug to javafx.fxml;
    exports com.example.sae.client.controller.components;
    opens com.example.sae.client.controller.components to javafx.fxml; // Add this line
}