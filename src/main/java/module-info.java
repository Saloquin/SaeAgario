module com.example.demo1wdassdfsdf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.accessibility;


    opens com.example.sae to javafx.fxml;
    exports com.example.sae;
    exports com.example.sae.enemyStrategy;
    opens com.example.sae.enemyStrategy to javafx.fxml;
}