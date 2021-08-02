package com.github.eybv.messenger.application.data;

import com.github.eybv.messenger.core.message.Message;
import com.github.eybv.messenger.core.message.MessageStatus;

import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;

@Data
@Builder
public class MessageData {

    private long id;
    private String senderId;
    private String recipientId;
    private String conversationId;
    private boolean delivered;
    private long timestamp;
    private String content;

    public static MessageData fromEntity(Message message) {
        MessageDataBuilder builder = MessageData.builder();
        builder.id(message.getId());
        builder.senderId(message.getSender().getId().toString());
        builder.recipientId(message.getRecipient().getId().toString());
        builder.conversationId(message.getConversation().toString());
        builder.delivered(message.getStatus().equals(MessageStatus.DELIVERED));

        long timestamp = message.getTimestamp()
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        builder.timestamp(timestamp);
        builder.content(message.getContent());

        return builder.build();
    }

}
