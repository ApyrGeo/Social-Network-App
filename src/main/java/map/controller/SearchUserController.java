package map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import map.domain.Prietenie;
import map.domain.Utilizator;
import map.domain.UtilizatorExtended;
import map.domain.exceptions.ServiceException;
import map.service.Service;
import map.utils.events.EntityChangeEvent;
import map.utils.observer.Observer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Predicate;

public class SearchUserController implements Observer<EntityChangeEvent> {

    public TextField searchField;
    public Label error;
    public AnchorPane suggestionArea;
    private Service service;
    private UtilizatorExtended muser;
    private HomePageController homePageController;

    public void init() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/home_page.fxml"));
        AnchorPane newPane = loader.load();

        homePageController = loader.getController();
        homePageController.setDivType("all");
        homePageController.setService(service);
        homePageController.setMainUtilizator(muser);
        homePageController.setPredicate(createPredicate());
        homePageController.init();

        suggestionArea.getChildren().add(newPane);

        searchField.textProperty().addListener(o -> handleFilter());
        handleFilter();
    }

    private Predicate<UtilizatorExtended> createPredicate(){
        Predicate<UtilizatorExtended> p1 = u -> u.getUname().startsWith(searchField.getText());
        Predicate<UtilizatorExtended> p2 = u -> {
            if(Objects.equals(u.getId(), muser.getId())) return false;

            else if(muser.getPrieteni().contains(u)){
                try {
                    Prietenie p = service.getPrietenie(muser.getId(), u.getId());
                    return Objects.equals(p.getStatus(), "rejected");

                }catch (ServiceException e) {
                    //pass
                }
                try{
                    Prietenie p = service.getPrietenie(u.getId(), muser.getId());
                    return Objects.equals(p.getStatus(), "rejected");
                } catch (ServiceException e) {
                    return true;
                }
            }
            else
                return true;
        };

        return p1.and(p2);
    }

    private void handleFilter() {
        homePageController.populateUsers();
    }

    public void handleSearch(ActionEvent actionEvent) {
        Utilizator user;
        try{
            user = service.getUtilizator(searchField.getText());
        }catch(ServiceException e) {
            error.setVisible(true);
            error.setText(e.getMessage());
            return;
        }

        try {
            service.addPrietenie(muser.getId(), user.getId(), LocalDateTime.now(), "pending");
        } catch (ServiceException e) {
            Prietenie existing = service.getPrietenie(muser.getId(), user.getId());
            if (Objects.equals(existing.getStatus(), "done"))
            {
                error.setVisible(true);
                error.setText("Prietenia exista deja!");
                return;
            }
            else
                service.updatePrietenie(existing.getId(), muser.getId(), user.getId(), "pending");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText("Cerere de prietenie trimisa!");
        alert.showAndWait();

    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setMainUser(UtilizatorExtended muser) {
        this.muser = muser;
    }

    @Override
    public void update(EntityChangeEvent event) {
        handleFilter();
    }
}
