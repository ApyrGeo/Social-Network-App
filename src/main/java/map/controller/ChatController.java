package map.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import map.domain.*;
import map.service.Service;
import map.utils.events.ChangeEventType;
import map.utils.events.EntityChangeEvent;
import map.utils.events.Event;
import map.utils.observer.Observer;
import map.utils.types.MessageType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatController implements Observer<EntityChangeEvent> {
    public VBox messageVBox;
    public TextField messageInputField;
    public VBox userVBox;
    public ScrollPane scrollPane;

    private List<Utilizator> friends;
    private List<Message> messages;
    private Service service;
    private UtilizatorExtended mainUser;

    private Utilizator selectedChatUser;
    private List<Utilizator> selectedUsers;
    private Message selectedMessage;

    public void addSelectedUser(Utilizator user) {
        messageVBox.getChildren().clear();
        selectedChatUser = null;
        selectedUsers.add(user);
    }

    public void removeSelectedUser(Utilizator user) {
        selectedUsers.remove(user);
    }

    public void init() throws IOException {
        selectedUsers = new ArrayList<>();
        initFriends();
        messageInputField.clear();

        scrollPane.vvalueProperty().bind(messageVBox.heightProperty());
    }

    public void setSelectedMessage(Message selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public Message getSelectedMessage(){
        return selectedMessage;
    }

    private void initFriends() {
        userVBox.getChildren().clear();
        friends = service.getFriends(mainUser.getId());
        friends.forEach(f -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/friend.fxml"));
                Parent box = loader.load();
                FriendController c = loader.getController();
                c.setUser(f);
                c.setParent(this);
                c.init();

                userVBox.getChildren().add(box);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initMessages() {
        messageVBox.getChildren().clear();
        Iterable<Message> messages = service.get2UsersMessages(selectedChatUser.getId(), mainUser.getId());

        if(messages == null) return;
        StreamSupport.stream(messages.spliterator(), false)
                .toList().forEach(m -> {
            if(m.getType() == MessageType.REPLY_MESSAGE) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/reply_message.fxml"));
                    Parent box = loader.load();
                    ReplyMessageController c = loader.getController();

                    Utilizator msg_user = service.getUtilizator(m.getFrom());

                    Utilizator initial_user = service.getUtilizator(((ReplyMessage) m).getInitialMessage().getFrom());
                    Message initial_message = ((ReplyMessage) m).getInitialMessage();

                    c.setMessage(new ReplyMessageDTO(msg_user, m, mainUser, initial_message, initial_user));
                    c.setParent(this);
                    c.init();

                    messageVBox.getChildren().add(box);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if(m.getType() == MessageType.MESSAGE) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../components/message.fxml"));
                    Parent box = loader.load();
                    MessageController c = loader.getController();

                    Utilizator msg_user = service.getUtilizator(m.getFrom());

                    c.setMessage(new MessageDTO(msg_user, m, mainUser));
                    c.setParent(this);
                    c.init();
                    messageVBox.getChildren().add(box);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void sendMessage(ActionEvent actionEvent) {
        String message = messageInputField.getText();
        messageInputField.clear();

        //TODO try catch

        List<Long> to = new ArrayList<>();
        if(selectedUsers != null && selectedChatUser == null) {
            to.addAll(selectedUsers.stream().map(Entity::getId).toList());
            service.addMessage(mainUser.getId(), to, message, LocalDateTime.now());
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Mesaj trimis la:");
                a.setContentText(friends.stream().map(Utilizator::getUname).collect(Collectors.joining("\n")));
                a.showAndWait();
            }
            initFriends();
        }
        else if(selectedMessage == null && selectedChatUser != null) {
            to.add(selectedChatUser.getId());
            service.addMessage(mainUser.getId(), to, message, LocalDateTime.now());
        }
        else if(selectedMessage != null && selectedChatUser != null) {
            to.add(selectedMessage.getFrom());
            service.addReplyMessage(mainUser.getId(), to, message, LocalDateTime.now(), selectedMessage.getId());
            selectedMessage = null;
        }
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
    }

    public void setMainUtilizator(UtilizatorExtended mainUser) {
        this.mainUser = mainUser;
    }


    public void openChat(Utilizator friend) {
        selectedChatUser = friend;
        selectedUsers.clear();
        initFriends();
        initMessages();
    }

    @Override
    public void update(EntityChangeEvent event) {
        if(event.getType() == ChangeEventType.MESSAGE_SENT && selectedChatUser != null) {
            initMessages();
        }
        else if(event.getData().getClass() == Utilizator.class || event.getData().getClass() == UtilizatorExtended.class) {
            initFriends();
        }
    }
}
