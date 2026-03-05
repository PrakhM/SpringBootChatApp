package com.example.demo.config;

import com.example.demo.chat.ChatMessage;
import com.example.demo.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener
{
    private final SimpMessageSendingOperations messageTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messageTemplate)
    {
        this.messageTemplate = messageTemplate;
    }

    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event)
    {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String)headerAccessor.getSessionAttributes().get("Username");
        if(username != null)
        {
            System.out.println("User disconnect: " + username);
            ChatMessage chatMessage = new ChatMessage(null, username, MessageType.LEAVE);
            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
