module com.example.sae {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.accessibility;

    opens com.example.sae to javafx.fxml;
    exports com.example.sae;
    exports com.example.sae.enemyStrategy;
    opens com.example.sae.enemyStrategy to javafx.fxml;
    exports com.example.sae.entity;
    opens com.example.sae.entity to javafx.fxml;
    opens com.example.sae.controller to javafx.fxml;
    exports com.example.sae.core.quadtree;
    opens com.example.sae.core.quadtree to javafx.fxml; // Add this line
}