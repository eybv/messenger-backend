package com.github.eybv.messenger.application.service.messaging;

import com.github.eybv.messenger.application.data.ConversationData;
import com.github.eybv.messenger.application.data.MessageData;

import java.util.List;

public interface MessagingService {

    MessageData sendMessage(String senderId, String recipientId, String content);

    MessageData findMessageById(Long id, String userId);

    List<MessageData> findAllMessages(String conversationId, String userId, Integer limit, Integer offset);

    ConversationData findConversationById(String conversationId, String userId);

    List<ConversationData> findAllConversations(String userId, Integer limit, Integer offset);

    void deleteConversation(String conversationId, String userId);

    void deleteMessage(Long messageId, String userId);

}
