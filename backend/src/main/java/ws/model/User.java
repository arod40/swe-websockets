package ws.model;

import lombok.Data;

@Data
public class User {
    private String username;
    private UserStatus status;
}
