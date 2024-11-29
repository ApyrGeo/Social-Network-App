package map.domain;

import map.utils.types.MessageType;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    
    private Message initial_message;
    
    public ReplyMessage(Long from, List<Long> to, String message, LocalDateTime data, Message initial_message) {
        super(from, to, message, data);
        this.initial_message = initial_message;
        super.type = MessageType.REPLY_MESSAGE;
    }

    public ReplyMessage(Long from, String message, LocalDateTime data, Message initial_message) {
        super(from, message, data);
        this.initial_message = initial_message;
        super.type = MessageType.REPLY_MESSAGE;
    }

    public Message getInitialMessage() {
        return initial_message;
    }
}
