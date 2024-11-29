package map.controller;

import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import map.domain.Utilizator;

public class FriendController {
    public Text name;
    public Button button;
    private Utilizator friend;
    private ChatController parent;
    private Boolean isSelected = false;

    public void handleButtonClick(ActionEvent actionEvent) {
        if(!isSelected) {
            selectButton();
        }
        else {
            deselectButton();
        }
    }

    private void selectButton() {
        isSelected = true;
        parent.addSelectedUser(friend);
        button.setStyle("""
                        -fx-font-size: 14px;
                        -fx-background-color: #54868780;
                        -fx-text-fill: white;
                        -fx-background-radius: 10;
                        -fx-border-radius: 10;
                        """);
    }

    private void deselectButton() {
        isSelected = false;
        parent.removeSelectedUser(friend);
        button.setStyle("""
                        -fx-font-size: 14px;
                        -fx-background-color: #548687;
                        -fx-text-fill: white;
                        -fx-background-radius: 10;
                        -fx-border-radius: 10;
                        """);
    }

    public void init(){
        name.setText(friend.getUname());
        deselectButton();
    }

    public void handleClick(MouseEvent mouseEvent) {
        parent.openChat(friend);
    }

    public void setUser(Utilizator f) {
        friend = f;
    }

    public void setParent(ChatController chatController) {
        parent = chatController;
    }
}
