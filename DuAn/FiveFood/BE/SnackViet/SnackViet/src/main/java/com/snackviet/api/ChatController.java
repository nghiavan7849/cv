package com.snackviet.api;

import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.snackviet.model.ChatMessage;

@Controller
public class ChatController {

    String userNickname = "";

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")  // Người dùng gửi tin nhắn tới admin
    public void sendMessageToAdmins(@Payload ChatMessage chatMessage) {
        userNickname = chatMessage.getNickname();
        messagingTemplate.convertAndSend("/topic/admins", chatMessage);
    }

    @MessageMapping("/reply")  // Admin trả lời người dùng
    public void replyToUser(@Payload ChatMessage chatMessage) {
        // Kênh dành riêng cho user
        String userChannel = "/topic/" + userNickname;
        messagingTemplate.convertAndSend(userChannel, chatMessage);
        System.out.println("Đây là userCHannel: "+userChannel);
    }
}

