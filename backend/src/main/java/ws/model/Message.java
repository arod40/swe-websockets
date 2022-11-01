package ws.model;

import lombok.Data;

import java.util.List;

@Data
public class Message {
    private String from;
    private MessageType type;
    private String content;
    private UserStatus status;
    private List<User> users;
}
