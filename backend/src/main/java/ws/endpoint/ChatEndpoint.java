package ws.endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import ws.model.Message;
import ws.model.User;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private static final Set<Session> chatEndpoints = Collections.synchronizedSet(new HashSet<>());

    private static HashMap<String, User> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        chatEndpoints.add(session);

        User user = new User();
        user.setUsername(username);
        user.setActive(true);
        users.put(session.getId(), user);

        System.out.println(username + " connected!!");
    }

    @OnMessage
    public void onMessage(Session session, Message message){
        System.out.println(users.get(session.getId()) + " says: " + message);
    }

    @OnClose
    public void onClose(Session session){
        chatEndpoints.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

}
