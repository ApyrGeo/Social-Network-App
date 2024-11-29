package map.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import map.domain.FriendshipStatus;
import map.domain.UtilizatorExtended;
import map.domain.UtilizatorPrietenieDTO;
import map.service.Service;
import map.utils.events.EntityChangeEvent;
import map.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HomePageController implements Observer<EntityChangeEvent> {
    @FXML
    public Label title;
    public AnchorPane main;
    public AnchorPane parent;
    @FXML
    private ListView<UtilizatorPrietenieDTO> list;
    @FXML
    private ListView<UtilizatorExtended> listu = new ListView<>();

    private List<UtilizatorPrietenieDTO> all_friendships = new ArrayList<>();
    private List<UtilizatorExtended> all_users = new ArrayList<>();

    private String div_type;
    private Service service;
    private UtilizatorExtended mainUser;
    private Predicate<UtilizatorExtended> predicate;

    private void AddToListViewAll(){
        all_users = service.getUtilizatori();
        populateUsers();
    }

    private void AddToListViewNormal() {
        all_friendships.clear();

        service.getFriendships().forEach(p -> {
            if (Objects.equals(p.getStatus(), FriendshipStatus.DONE) && Objects.equals(div_type, "normal")) {
                if (Objects.equals(p.getIdPrieten1(), mainUser.getId()))
                    all_friendships.add(new UtilizatorPrietenieDTO(service.getUtilizator(p.getIdPrieten2()), p.getIdPrieten1(), p.getIdPrieten2(), p.getFriendsFrom(), p.getStatus()));

                else if (Objects.equals(p.getIdPrieten2(), mainUser.getId()))
                    all_friendships.add(new UtilizatorPrietenieDTO(service.getUtilizator(p.getIdPrieten1()), p.getIdPrieten2(), p.getIdPrieten1(), p.getFriendsFrom(), p.getStatus()));
            }
        });

        populateFriendships();
    }

    private void AddToListViewRequests() {
        all_friendships.clear();

        service.getFriendships().forEach(p -> {
            if((Objects.equals(p.getStatus(), FriendshipStatus.PENDING)) && Objects.equals(div_type, "requests")) {
                if(Objects.equals(p.getIdPrieten2(), mainUser.getId())) {
                    all_friendships.add(new UtilizatorPrietenieDTO(service.getUtilizator(p.getIdPrieten1()), p.getIdPrieten2(), p.getIdPrieten1(), p.getFriendsFrom(), p.getStatus()));
                }
            }
        });
        service.getFriendships().forEach(p -> {
            if((Objects.equals(p.getStatus(), FriendshipStatus.REJECTED)) && Objects.equals(div_type, "requests")) {
                if(Objects.equals(p.getIdPrieten2(), mainUser.getId())) {
                    all_friendships.add(new UtilizatorPrietenieDTO(service.getUtilizator(p.getIdPrieten1()), p.getIdPrieten2(), p.getIdPrieten1(), p.getFriendsFrom(), p.getStatus()));
                }
            }
        });

        populateFriendships();
    }
    public void setPredicate(Predicate<UtilizatorExtended> predicate) {
        this.predicate = predicate;
    }

    public void populateFriendships() {
        list.getItems().setAll(all_friendships);
    }

//    public void populateFriendships(Predicate<UtilizatorPrietenieDTO> u) {
//        list.getItems().setAll(all_friendships.stream().filter(u).collect(Collectors.toList()));
//    }

    public void populateUsers() {
        listu.getItems().setAll(all_users.stream().filter(predicate).collect(Collectors.toList()));
    }


    public void init() {

        if(Objects.equals(div_type, "normal")){
            title.setText("Lista prieteni:");
            AddToListViewNormal();
            list.setCellFactory(param -> new UserPrietenieDiv(div_type, service, mainUser));
        }

        else if(Objects.equals(div_type, "requests")) {
            title.setText("Cereri prietenie:");
            AddToListViewRequests();
            list.setCellFactory(param -> new UserPrietenieDiv(div_type, service, mainUser));
        }

        else if(Objects.equals(div_type, "all")){
            title.setText("Sugestii:");
            parent.getChildren().setAll(listu);
            AddToListViewAll();
            listu.setPrefHeight(400);
            listu.setPrefWidth(400);
            AnchorPane.setLeftAnchor(listu, 0.0);


            listu.setCellFactory(param -> new UserDiv(service, mainUser));
        }
    }

    public void setDivType(String div_type) {
        this.div_type = div_type;
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
    }

    public void setMainUtilizator(UtilizatorExtended mainUser) {
        this.mainUser = mainUser;
    }

    @Override
    public void update(EntityChangeEvent event) {
        all_friendships.clear();
        all_users.clear();

        if(Objects.equals(div_type, "normal")){
            AddToListViewNormal();
        }
        else if(Objects.equals(div_type, "all")){
            AddToListViewAll();
        }
        else if(Objects.equals(div_type, "requests")) {
            AddToListViewRequests();
        }
    }
}
