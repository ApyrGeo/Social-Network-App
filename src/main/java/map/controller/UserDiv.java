package map.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import map.domain.UtilizatorExtended;
import map.domain.UtilizatorPrietenieDTO;
import map.service.Service;

import java.io.IOException;
import java.util.Objects;

public class UserDiv extends ListCell<UtilizatorExtended> {
    private String type;
    private Parent root;
    private UserDivAllController c;
    private Service service;
    private UtilizatorExtended muser;


    public UserDiv(Service service, UtilizatorExtended muser) {
        setStyle("-fx-background-color: #00000000");
        this.service = service;
        this.muser = muser;

        FXMLLoader loader;
        loader = new FXMLLoader(getClass().getResource("../components/user_div_all.fxml"));

        try {
            root = loader.load();
            c = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void updateItem(UtilizatorExtended item, boolean empty){
        super.updateItem(item, empty);

        if(empty || item == null){
            setGraphic(null);
        }
        else{
            c.setUser(item);
            c.setService(service);
            c.setMainUser(muser);
            setGraphic(root);
        }
    }
}
