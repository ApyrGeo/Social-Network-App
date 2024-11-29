package map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import map.domain.Prietenie;
import map.domain.UtilizatorPrietenieDTO;
import map.domain.exceptions.ServiceException;
import map.service.Service;
import map.utils.Constants;

public class UserDivNormalController{
    public Label name;
    @FXML
    public Circle circle;
    private Service service;
    private Long muserId;
    private Long userId;
    private String status;


    public void setDTO(UtilizatorPrietenieDTO u) {
        muserId = u.getIdUser1();
        userId = u.getIdUser2();
        name.setText(u.getFullName() + "-Friends since: " + u.getFrom().format(Constants.DATE_FORMATTER));
        setCircleColor("#FFFFFF");
    }

    public void setCircleColor(String hexcode) {
        circle.setStyle("-fx-fill: " + hexcode + ";");
    }

    public void handleRemove(ActionEvent actionEvent) {
        //System.out.println("Remove user");

        try {
            Prietenie p = service.getPrietenie(muserId, userId);
            service.removePrietenie(p.getId());

        }catch (ServiceException e) {
            Prietenie p = service.getPrietenie(userId, muserId);
            service.removePrietenie(p.getId());
        }
    }

    public void setService(Service service) {
        this.service = service;
    }

}
