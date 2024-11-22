package map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import map.domain.UtilizatorExtended;
import map.service.Service;

import java.io.IOException;

public class MenuController {

    public Button home;
    public Button addfriend;
    public Button requests;
    public BorderPane mainArea;
    @FXML
    private VBox sidePanel;
    @FXML
    private Button toggleButton;
    @FXML
    private SplitPane splitPane;

    private boolean isPanelExpanded = false;
    private Service service;
    private UtilizatorExtended mainUser;
    private Scene scene;

    private double min_ratio, max_ratio;

    public void init() {
        hideText();

        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(isPanelExpanded)
                toggleOpenClose();

            min_ratio = toggleButton.getWidth() / newValue.doubleValue() + 0.008;
            max_ratio = (requests.getWidth() + toggleButton.getWidth()) / newValue.doubleValue() + 0.008;
            splitPane.setDividerPositions(min_ratio);
        });

    }

    private void hideText() {
        home.setVisible(false);
        addfriend.setVisible(false);
        requests.setVisible(false);
    }

    private void revealText() {
        home.setVisible(true);
        addfriend.setVisible(true);
        requests.setVisible(true);
    }

    @FXML
    public void handleOpenCloseMenu(ActionEvent actionEvent) {
        toggleOpenClose();
    }

    private void toggleOpenClose() {
        if (isPanelExpanded) {
            hideText();
            splitPane.setDividerPositions(min_ratio, 0.4 + min_ratio);
            toggleButton.setText("≡");
        } else {
            revealText();
            splitPane.setDividerPositions(min_ratio + requests.getWidth(), 0.2 + min_ratio + requests.getWidth());
            toggleButton.setText("▷");
        }
        isPanelExpanded = !isPanelExpanded;
    }

    public void handleMainMenu(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/home_page.fxml"));
            AnchorPane newPane = loader.load();

            HomePageController c = loader.getController();
            c.setDivType("normal");
            c.setService(service);
            c.setMainUtilizator(mainUser);

            c.init();

            mainArea.setCenter(newPane);

            toggleOpenClose();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void handleAdd(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/search_user.fxml"));
            AnchorPane newPane = loader.load();

            SearchUserController c = loader.getController();
            c.setService(service);
            c.setMainUser(mainUser);
            c.init();

            mainArea.setCenter(newPane);

            toggleOpenClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRequests(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/home_page.fxml"));
            AnchorPane newPage = loader.load();

            HomePageController c = loader.getController();
            c.setDivType("requests");
            c.setService(service);
            c.setMainUtilizator(mainUser);

            c.init();

            mainArea.setCenter(newPage);



            toggleOpenClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setMainUser(UtilizatorExtended u) {
        this.mainUser = u;
    }


    public void setScene(Scene scene) {
        this.scene = scene;
    }


}
