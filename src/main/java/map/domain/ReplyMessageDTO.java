package map.domain;

public class ReplyMessageDTO extends MessageDTO {
    private String initialUser;
    private String initialMessage;
    
    public ReplyMessageDTO(Utilizator utilizator, Message message, Utilizator crtUser, Message initialMessage, Utilizator replyUser) {
        super(utilizator, message, crtUser);
        this.initialUser = replyUser.getFirstName() + " " + replyUser.getLastName();
        this.initialMessage = initialMessage.getMessage();
    }

    public String getInitialUser() {
        return initialUser;
    }
    public String getInitialMessage() {
        return initialMessage;
    }
}
