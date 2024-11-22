package map.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import map.domain.UtilizatorExtended;
import map.domain.UtilizatorPrietenieDTO;
import map.service.Service;

import java.io.IOException;
import java.util.Objects;

public class UserPrietenieDiv extends ListCell<UtilizatorPrietenieDTO> {
    private String type;
    private Parent root;
    UserDivReqController controller_req;
    UserDivNormalController controller_normal;

    private Service service;
    private UtilizatorExtended muser;


    public UserPrietenieDiv(String type, Service service, UtilizatorExtended muser) {
        setStyle("-fx-background-color: #00000000");
        this.service = service;
        this.muser = muser;
        this.type = type;

        FXMLLoader loader;
        if(Objects.equals(type, "requests"))
            loader = new FXMLLoader(getClass().getResource("../components/user_div_req.fxml"));
        else
            loader = new FXMLLoader(getClass().getResource("../components/user_div_normal.fxml"));

        try {
            root = loader.load();
            if(Objects.equals(type, "requests"))
                controller_req = loader.getController();
            else
                controller_normal = loader.getController();

        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void updateItem(UtilizatorPrietenieDTO item, boolean empty){
        super.updateItem(item, empty);

        if(empty || item == null){
            setGraphic(null);
        }
        else {
            if(Objects.equals(type, "requests"))
            {
                controller_req.setService(service);
                controller_req.setDTO(item);
            }
            else
            {
                controller_normal.setService(service);
                controller_normal.setDTO(item);
            }

            setGraphic(root);
        }
    }
}
