module com.example.socialmediaapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.socialmediaapp to javafx.fxml;
    exports com.example.socialmediaapp;
}