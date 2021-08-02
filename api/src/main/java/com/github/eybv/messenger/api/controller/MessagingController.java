package com.github.eybv.messenger.api.controller;

import com.github.eybv.messenger.api.validation.UUID;
import com.github.eybv.messenger.application.data.ConversationData;
import com.github.eybv.messenger.application.data.MessageData;
import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.service.messaging.MessagingService;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/messaging")
public class MessagingController {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessagingService messagingService;

    public MessagingController(SimpMessagingTemplate messagingTemplate,
                               MessagingService messagingService) {
        this.messagingTemplate = messagingTemplate;
        this.messagingService = messagingService;
    }

    @PostMapping("/to/{id}")
    public void sendMessage(@AuthenticationPrincipal UserData sender,
                            @PathVariable("id") @UUID String recipientId,
                            @RequestBody @NotBlank String content) {

        MessageData message = messagingService.sendMessage(sender.getId(), recipientId, content);

        messagingTemplate.convertAndSendToUser(
                message.getRecipientId(), "/conversations",
                message.getConversationId()
        );

        messagingTemplate.convertAndSendToUser(
                message.getSenderId(), "/conversations",
                message.getConversationId()
        );

        messagingTemplate.convertAndSendToUser(
                message.getConversationId(), "/messages",
                message.getId()
        );
    }

    @GetMapping("/messages/{id}")
    public MessageData getMessageById(@AuthenticationPrincipal UserData user,
                                      @PathVariable("id") Long messageId) {
        return messagingService.findMessageById(messageId, user.getId());
    }

    @GetMapping("/conversations/{id}/messages")
    public List<MessageData> getMessageList(@AuthenticationPrincipal UserData user,
                                            @PathVariable("id") @UUID String conversationId,
                                            Integer limit, Integer offset) {
        return messagingService.findAllMessages(conversationId, user.getId(), limit, offset);
    }

    @GetMapping("/conversations/{id}")
    public ConversationData getConversationById(@AuthenticationPrincipal UserData user,
                                                @PathVariable("id") @UUID String conversationId) {
        return messagingService.findConversationById(conversationId, user.getId());
    }

    @GetMapping("/conversations")
    public List<ConversationData> getConversationList(@AuthenticationPrincipal UserData user,
                                                      Integer limit, Integer offset) {
        return messagingService.findAllConversations(user.getId(), limit, offset);
    }

    @DeleteMapping("/conversations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConversation(@AuthenticationPrincipal UserData user,
                                   @PathVariable("id") @UUID String conversationId) {
        messagingService.deleteConversation(conversationId, user.getId());
    }

    @DeleteMapping("/messages/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@AuthenticationPrincipal UserData user,
                              @PathVariable("id") Long messageId) {
        messagingService.deleteMessage(messageId, user.getId());
    }

}
