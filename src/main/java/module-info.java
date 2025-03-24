module com.example.demo1wdassdfsdf {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.accessibility;


    opens com.example.demo1wdassdfsdf to javafx.fxml;
    exports com.example.demo1wdassdfsdf;
}