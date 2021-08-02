package com.github.eybv.messenger.core.message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

    Optional<Message> findById(long id);

    Optional<Message> findLastNotDeletedByConversationAndUserId(UUID conversation, UUID userId);

    List<Message> findAllNotDeletedByConversationAndUserId(UUID conversation, UUID userId, int limit, int offset);

    long countNewByConversationAndRecipientId(UUID conversation, UUID recipientId);

    Message save(Message message);

    void delete(Message message);

}
