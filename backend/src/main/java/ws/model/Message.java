package ws.model;

import lombok.Data;

@Data
public class Message {
    private String from;
    private MessageType type;
    private String content;
    private UserStatus status;
}
