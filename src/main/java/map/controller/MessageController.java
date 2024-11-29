package map.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import map.domain.MessageDTO;
import map.utils.Constants;

public class MessageController {

    public VBox messageBubble;
    public Text text;
    public boolean isSentByUser;
    public HBox main;
    public Text name;
    public Text date;
    public VBox container;
    public ChatController chatController;

    private MessageDTO message;
    private Boolean isSelected = false;


    public void init(){
        if (isSentByUser) {
            main.setAlignment(Pos.CENTER_RIGHT);
        } else {
            main.setAlignment(Pos.CENTER_LEFT);
        }
    }

    public void setMessage(MessageDTO message) {
        isSentByUser = message.getLoggedUser();
        name.setText(message.getUName());
        date.setText(message.getDate().format(Constants.TIME_FORMATTER));
        text.setText(message.getMessage());
        this.message = message;
    }

    public void handleReply(MouseEvent mouseEvent) {
        //System.out.println("Intra");
        if(isSelected){
            deselectMessage();
            chatController.setSelectedMessage(null);
            isSelected = false;
        }
        else {
            if(chatController.getSelectedMessage() == null){
                selectMessage();
                chatController.setSelectedMessage(message.getOriginalMessage());
                isSelected = true;
            }
            else {
                Alert a =  new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning");
                a.setHeaderText(null);
                a.setContentText("Este deja selectat un mesaj pentru reply!");
                a.showAndWait();
            }
        }
    }

    private void selectMessage() {
        container.setStyle("-fx-background-color: #F5CB5C80; -fx-border-radius: 6; -fx-background-radius: 6;");
        messageBubble.setStyle("-fx-background-color: #54868780; -fx-padding: 10; -fx-background-radius: 15; -fx-max-width: 400px;");
    }

    private void deselectMessage() {
        container.setStyle("-fx-background-color: #F5CB5C; -fx-border-radius: 6; -fx-background-radius: 6;");
        messageBubble.setStyle("-fx-background-color: #548687; -fx-padding: 10; -fx-background-radius: 15; -fx-max-width: 400px;");
    }

    public void setParent(ChatController box) {
        chatController = box;
    }
}
