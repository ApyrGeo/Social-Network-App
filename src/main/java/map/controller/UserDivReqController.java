package map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import map.domain.*;
import map.service.Service;

import java.util.Objects;

public class UserDivReqController {
    public Label name;
    @FXML
    public Circle circle;
    public Button accept;
    public Button decline;
    private Long muserId;
    private Service service;
    private Long userId;
    private FriendshipStatus status;

    private void disableControls() {
        accept.setDisable(true);
        decline.setDisable(true);
        accept.setVisible(false);
        decline.setVisible(false);
    }
    private void enableControls(){
        accept.setDisable(false);
        decline.setDisable(false);
        accept.setVisible(true);
        decline.setVisible(true);
    }

    public void setDTO(UtilizatorPrietenieDTO u) {
        userId = u.getIdUser2();
        muserId = u.getIdUser1();
        status = u.getStatus();

        name.setText(u.getFullName() + "\tSent at: " + u.getFrom().toLocalDate());
        setPendingDeclined();
    }

    public void setPendingDeclined() {
        if(Objects.equals(status, FriendshipStatus.REJECTED)) {
            circle.setStyle("-fx-fill: red ;");
            disableControls();
        }
        else {
            circle.setStyle("-fx-fill: green;");
            enableControls();
        }
    }

    public void handleAccept(ActionEvent actionEvent) {
        Prietenie wanted = service.getPrietenie(userId, muserId);

        service.updatePrietenie(wanted.getId(), wanted.getIdPrieten1(), wanted.getIdPrieten2(), FriendshipStatus.DONE);

    }

    public void setService(Service service) {
        this.service = service;
    }

    public void handleDecline(ActionEvent actionEvent) {
        Prietenie wanted = service.getPrietenie(userId, muserId);

        service.updatePrietenie(wanted.getId(), wanted.getIdPrieten1(), wanted.getIdPrieten2(), FriendshipStatus.REJECTED);
    }
}
