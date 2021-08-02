package com.github.eybv.messenger.persistence;

import com.github.eybv.messenger.core.message.Message;
import com.github.eybv.messenger.core.message.MessageRepository;
import com.github.eybv.messenger.core.message.MessageStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaMessageRepository extends MessageRepository, JpaRepository<Message, Long> {

    long countByStatusAndConversationAndRecipientId(MessageStatus status, UUID conversation, UUID recipientId);

    @Override
    default long countNewByConversationAndRecipientId(UUID conversation, UUID recipientId) {
        return countByStatusAndConversationAndRecipientId(MessageStatus.RECEIVED, conversation, recipientId);
    }

    @Override
    default Optional<Message> findLastNotDeletedByConversationAndUserId(UUID conversation, UUID userId) {
        return findAllNotDeletedByConversationAndUserId(conversation, userId, 1, 0).stream().findFirst();
    }

    @Override
    @Query(value = "SELECT * FROM messages m " +
            "WHERE m.conversation = :conversation " +
            "AND (m.sender_id = :user_id OR m.recipient_id = :user_id)" +
            "AND m.id NOT IN (" +
                "SELECT message_id FROM deleted_messages " +
                "WHERE user_id = :user_id" +
            ") " +
            "ORDER BY `timestamp` DESC " +
            "LIMIT :offset , :limit", nativeQuery = true)
    List<Message> findAllNotDeletedByConversationAndUserId(
            @Param("conversation") UUID conversation,
            @Param("user_id") UUID userId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

}
