package ws.endpoint;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import ws.model.Message;
import ws.model.MessageType;
import ws.model.User;
import ws.model.UserStatus;

@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private static final Set<Session> chatEndpoints = Collections.synchronizedSet(new HashSet<>());

    private static HashMap<String, User> users = new HashMap<>();

    private static Logger logger = Logger.getLogger(ChatEndpoint.class.getName());

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        // Add new user to Server session
        chatEndpoints.add(session);
        User user = new User();
        user.setUsername(username);
        user.setStatus(UserStatus.ONLINE);
        users.put(session.getId(), user);

        // Tell the new user the list of other users
        Message userListMessage = new Message();
        userListMessage.setType(MessageType.USER_LIST);
        userListMessage.setUsers(users.values().stream().toList());
        sendMessage(session, userListMessage);

        // Broadcast new user joined
        Message newOnlineUserMessage = new Message();
        newOnlineUserMessage.setType(MessageType.STATUS_CHANGE);
        newOnlineUserMessage.setFrom(user.getUsername());
        newOnlineUserMessage.setStatus(UserStatus.ONLINE);
        broadcast(newOnlineUserMessage);

        logger.log(Level.INFO, username + " connected!!");
    }

    @OnMessage
    public void onMessage(Session session, Message message){
        logger.log(Level.INFO, message.toString());
        if (message.getType() == MessageType.STATUS_CHANGE) {
            users.get(session.getId()).setStatus(message.getStatus());
        }
        broadcast(message);

    }

    @OnClose
    public void onClose(Session session){
        chatEndpoints.remove(session);

        // Broadcast new user left
        User user = users.get(session.getId());
        Message offlineUserMessage = new Message();
        offlineUserMessage.setType(MessageType.STATUS_CHANGE);
        offlineUserMessage.setFrom(user.getUsername());
        offlineUserMessage.setStatus(UserStatus.OFFLINE);
        broadcast(offlineUserMessage);

        logger.log(Level.INFO, user.getUsername() + " connected!!");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(Message message){
        chatEndpoints.forEach(session -> {
            if (users.get(session.getId()).getStatus() == UserStatus.ONLINE)
                sendMessage(session, message);
        });
    }

    private static void sendMessage(Session session, Message message){
        synchronized (session) {
            try {
                session.getBasicRemote()
                        .sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        }
    }
}
