package map.domain;

import map.utils.types.MessageType;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long>{
    private Long from;
    private List<Long> to;
    private String message;
    private LocalDateTime data;
    protected MessageType type = MessageType.MESSAGE;

    public Message(Long id_from, List<Long> id_to, String message, LocalDateTime data) {
        this.from = id_from;
        this.to = id_to;
        this.message = message;
        this.data = data;
    }
    public Message(Long id_from, String message, LocalDateTime data) {
        this.from = id_from;
        this.to = new ArrayList<>();
        this.message = message;
        this.data = data;
    }

    public void addTo(Long id){
        to.add(id);
    }
    public Long getFrom() {
        return from;
    }
    public List<Long> getTo() {
        return to;
    }
    public String getMessage() {
        return message;
    }
    public LocalDateTime getData() {
        return data;
    }
    public MessageType getType() {
        return type;
    }
}

