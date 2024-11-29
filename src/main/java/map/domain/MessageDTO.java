package map.domain;

import java.awt.*;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class MessageDTO {
    private Message original_message;
    private String name;
    private String message;
    private LocalDateTime timestamp;
    private Boolean isLoggedUser;

    public MessageDTO(Utilizator utilizator, Message message, Utilizator crtUser) {
        this.isLoggedUser = (Objects.equals(utilizator.getId(), crtUser.getId()));
        this.name = utilizator.getFirstName() + " " + utilizator.getLastName();
        this.message = message.getMessage();
        this.timestamp = message.getData();
        this.original_message = message;
    }

    public String getUName() {
        return name;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getDate() {
        return timestamp;
    }
    public Boolean getLoggedUser() {
        return isLoggedUser;
    }
    public Message getOriginalMessage() {
        return original_message;
    }
}

