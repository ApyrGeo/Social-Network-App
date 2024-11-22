package map.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.domain.exceptions.ServiceException;
import map.service.Service;

public class RegisterController {
    public TextField usernameField;
    public TextField firstnameField;
    public TextField lastnameField;
    public PasswordField passwordField;
    public Label errorLabel;
    private Service service;
    private Stage stage;

    public void setService(Service service) {
        this.service = service;
    }

    public void handleRegister(ActionEvent actionEvent) {
        try {
            service.addUtilizator(firstnameField.getText(), lastnameField.getText(), usernameField.getText(), passwordField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully registered");
            alert.showAndWait();

            stage.close();
        }
        catch (RuntimeException e) {

            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }

    public void setStage(Stage dialogStage) {
        this.stage = dialogStage;
    }
}
