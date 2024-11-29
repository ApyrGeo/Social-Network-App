package map.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import map.domain.ReplyMessage;
import map.domain.ReplyMessageDTO;
import map.utils.Constants;

public class ReplyMessageController {


    public Text originalUserName;
    public Text originalMessageText;
    public Text replyUserName;
    public Text replyText;
    public HBox main;
    public Text date;
    public VBox container;
    public VBox replyMessageContainer;
    private Boolean isSentByUser;
    private ReplyMessageDTO message;
    private ChatController chatController;
    private Boolean isSelected = false;

    public void init(){
        if (isSentByUser) {
            main.setAlignment(Pos.CENTER_RIGHT);
        } else {
            main.setAlignment(Pos.CENTER_LEFT);
        }
    }

    public void setMessage(ReplyMessageDTO message) {
        isSentByUser = message.getLoggedUser();
        replyText.setText(message.getMessage());
        replyUserName.setText(message.getUName());
        date.setText(message.getDate().format(Constants.TIME_FORMATTER));

        originalMessageText.setText(message.getInitialMessage());
        originalUserName.setText(message.getInitialUser());
        this.message = message;
    }

    public void setParent(ChatController box){
        chatController = box;
    }

    private void selectMessage() {
        container.setStyle("-fx-background-color: #F5CB5C80; -fx-border-radius: 6; -fx-background-radius: 6;");
        replyMessageContainer.setStyle("-fx-background-color: #54868780; -fx-padding: 10; -fx-background-radius: 15;");
    }

    private void deselectMessage() {
        container.setStyle("-fx-background-color: #F5CB5C; -fx-border-radius: 6; -fx-background-radius: 6;");
        replyMessageContainer.setStyle("-fx-background-color: #548687; -fx-padding: 10; -fx-background-radius: 15;");
    }

    public void handleReply(MouseEvent mouseEvent) {
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
}
