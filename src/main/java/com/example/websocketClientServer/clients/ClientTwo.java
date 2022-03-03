package com.example.websocketClientServer.clients;

import com.example.websocketClientServer.server.IncomingMessage;
import com.example.websocketClientServer.server.OutgoingMessage;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

public class ClientTwo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        ClientTwoSessionHandler clientTwoSessionHandler = new ClientTwoSessionHandler();
        ListenableFuture<StompSession> sessionAsync = stompClient.connect("ws://localhost:8097/websocket-server", clientTwoSessionHandler);
        StompSession session = sessionAsync.get();
        session.subscribe("/topic/messages", clientTwoSessionHandler);
        while (true) {
            session.send("/topic/messages", new IncomingMessage("Almas" + System.currentTimeMillis()));
            Thread.sleep(2000);
        }
    }
}
class ClientTwoSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return IncomingMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Received"+ ((IncomingMessage)payload).getName());
    }
}


