package ws.endpoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{username}")
public class ChatEndpoint {
    private Session session;
    private static final Set<Session> chatEndpoints = Collections.synchronizedSet(new HashSet<>());

    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        this.session = session;
        chatEndpoints.add(session);
        users.put(session.getId(), username);

        System.out.println(username + " connected!!");
    }

    @OnMessage
    public void onMessage(Session session, String message){
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
