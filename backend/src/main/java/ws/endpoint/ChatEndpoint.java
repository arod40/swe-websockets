package ws.endpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import ws.model.Message;
import ws.model.User;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private static final Set<Session> chatEndpoints = Collections.synchronizedSet(new HashSet<>());

    private static HashMap<String, User> users = new HashMap<>();

    private static Logger logger = Logger.getLogger(ChatEndpoint.class.getName());

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        chatEndpoints.add(session);

        User user = new User();
        user.setUsername(username);
        user.setActive(true);
        users.put(session.getId(), user);
        logger.log(Level.INFO, username + " connected!!");
    }

    @OnMessage
    public void onMessage(Session session, Message message){
        logger.log(Level.INFO, message.toString());
        broadcast(message);

    }

    @OnClose
    public void onClose(Session session){
        chatEndpoints.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(Message message){
        chatEndpoints.forEach(session -> {
            synchronized (session) {
                try {
                    session.getBasicRemote()
                            .sendObject(message);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
