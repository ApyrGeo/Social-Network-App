package map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import map.domain.Utilizator;
import map.domain.UtilizatorExtended;
import map.domain.exceptions.ServiceException;
import map.service.Service;

import java.io.IOException;

public class LogInController {
    public PasswordField passwordField;
    public TextField usernameField;
    public Service service;
    public Label errorLabel;
    private Scene scene;

    public void setService(Service s) {
        service = s;
    }

    public void handleLogin(ActionEvent actionEvent) {
        errorLabel.setVisible(false);

        String username = usernameField.getText();
        String password = passwordField.getText();

        try{
            Utilizator u = service.authentificate(username, password);
            redirectMainMenu(u);
        }
        catch(ServiceException e){
            errorLabel.setVisible(true);
            errorLabel.setText(e.getMessage());
        }
    }

    public void handleRegister(ActionEvent actionEvent) {
        redirectRegister();
    }

    private void redirectRegister() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/register.fxml"));

            GridPane root = (GridPane) loader.load();
            Scene scene = new Scene(root);

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Register");
            dialogStage.setWidth(350);
            dialogStage.setHeight(350);
            dialogStage.setScene(scene);



            RegisterController controller = loader.getController();
            controller.setService(service);
            controller.setStage(dialogStage);

            dialogStage.show();

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectMainMenu(Utilizator u) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../views/menu.fxml"));

            BorderPane root = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setWidth(1000);
            dialogStage.setHeight(600);
            dialogStage.setTitle("Main menu: " + u.getUname());
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MenuController menuController = loader.getController();
            menuController.setService(service);
            menuController.setMainUser(service.getUtilizator(u.getId()));
            menuController.setScene(scene);

            menuController.init();

            dialogStage.show();

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
