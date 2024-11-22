package map.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import map.domain.Prietenie;
import map.domain.UtilizatorExtended;
import map.domain.exceptions.ServiceException;
import map.service.Service;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDivAllController {
    public Circle circle;
    public Label name;
    public Button accept;
    private Service service;
    private UtilizatorExtended user;
    private UtilizatorExtended muser;

    public void setUser(UtilizatorExtended item) {
        this.user = item;
        name.setText(item.getUname() + " - " + item.getFirstName() + " " + item.getLastName());

    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setMainUser(UtilizatorExtended muser) {
        this.muser = muser;
    }

    public void handleAdd(ActionEvent actionEvent) {
        try {
            service.addPrietenie(muser.getId(), user.getId(), LocalDateTime.now(), "pending");

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Info");
            a.setHeaderText(null);
            a.setContentText("Prietenie trimisa cu succes");
            a.showAndWait();

        } catch (ServiceException e) {
            Prietenie existing = service.getPrietenie(muser.getId(), user.getId());
            if (!Objects.equals(existing.getStatus(), "done")) {
                service.updatePrietenie(existing.getId(), muser.getId(), user.getId(), "pending");

                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Info");
                a.setHeaderText(null);
                a.setContentText("Prietenie trimisa cu succes");
                a.showAndWait();
            }
        }
    }
}
