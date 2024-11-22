module map {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jdk.jshell;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires java.sql;
    requires java.desktop;

    opens map to javafx.fxml;
    exports map;
    exports map.controller;
    opens map.controller to javafx.fxml;

}