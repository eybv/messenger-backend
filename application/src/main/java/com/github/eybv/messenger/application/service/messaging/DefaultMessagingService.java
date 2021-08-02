package com.github.eybv.messenger.application.service.messaging;

import com.github.eybv.messenger.application.data.ConversationData;
import com.github.eybv.messenger.application.data.MessageData;
import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.excaption.OperationRejectedException;
import com.github.eybv.messenger.application.excaption.ResourceNotFoundException;
import com.github.eybv.messenger.application.service.uuid.UUIDService;
import com.github.eybv.messenger.core.message.*;
import com.github.eybv.messenger.core.user.User;
import com.github.eybv.messenger.core.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultMessagingService implements MessagingService {

    private final UUIDService uuidService;

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final DeletedMessageRepository deletedMessageRepository;

    public DefaultMessagingService(UUIDService uuidService,
                                   UserRepository userRepository,
                                   MessageRepository messageRepository,
                                   DeletedMessageRepository deletedMessageRepository) {
        this.uuidService = uuidService;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.deletedMessageRepository = deletedMessageRepository;
    }

    @Override
    public MessageData sendMessage(String senderId, String recipientId, String content) {
        User sender = userRepository
                .findById(UUID.fromString(senderId))
                .orElseThrow(() -> throwUserNotFound(senderId));

        User recipient = userRepository
                .findById(UUID.fromString(recipientId))
                .orElseThrow(() -> throwUserNotFound(senderId));

        UUID conversation = uuidService.combine(sender.getId(), recipient.getId());

        sender.getConversations().add(conversation);
        recipient.getConversations().add(conversation);

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setConversation(conversation);
        message.setContent(content);

        userRepository.save(sender);
        userRepository.save(recipient);

        message = messageRepository.save(message);

        return MessageData.fromEntity(message);
    }

    @Override
    public MessageData findMessageById(Long id, String userId) {
        UUID _userId = UUID.fromString(userId);
        return messageRepository
                .findById(id)
                .map(message -> {
                    if (!message.getRecipient().getId().equals(_userId) &&
                            !message.getSender().getId().equals(_userId)) {
                        throw new OperationRejectedException("Forbidden");
                    }
                    if (message.getRecipient().getId().equals(_userId)) {
                        message.setStatus(MessageStatus.DELIVERED);
                        message = messageRepository.save(message);
                    }
                    return MessageData.fromEntity(message);
                })
                .orElseThrow(() -> throwMessageNotFound(id));
    }

    @Override
    public List<MessageData> findAllMessages(String conversationId, String userId, Integer limit, Integer offset) {
        limit = Optional.ofNullable(limit).orElse(100);
        offset = Optional.ofNullable(offset).orElse(0);

        UUID conversation = UUID.fromString(conversationId);
        UUID _userId = UUID.fromString(userId);

        return messageRepository
                .findAllNotDeletedByConversationAndUserId(conversation, _userId, limit, offset)
                .stream()
                .map(message -> {
                    if (message.getRecipient().getId().equals(_userId)) {
                        message.setStatus(MessageStatus.DELIVERED);
                        message = messageRepository.save(message);
                    }
                    return MessageData.fromEntity(message);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ConversationData findConversationById(String conversationId, String userId) {
        UUID _conversationId = UUID.fromString(conversationId);
        UUID _userId = UUID.fromString(userId);

        return userRepository
                .findById(_userId)
                .orElseThrow(() -> throwUserNotFound(userId))
                .getConversations()
                .stream()
                .filter(x -> x.equals(_conversationId))
                .findFirst()
                .map(conversation -> mapConversation(conversation, _userId))
                .orElseThrow(() -> throwConversationNotFound(conversationId));
    }

    @Override
    public List<ConversationData> findAllConversations(String userId, Integer limit, Integer offset) {
        limit = Optional.ofNullable(limit).orElse(100);
        offset = Optional.ofNullable(offset).orElse(0);
        UUID _userId = UUID.fromString(userId);

        return userRepository
                .findById(_userId)
                .orElseThrow(() -> throwUserNotFound(userId))
                .getConversations()
                .stream()
                .skip(offset).limit(limit)
                .map(conversation -> mapConversation(conversation, _userId))
                .collect(Collectors.toList());
    }

    private ConversationData mapConversation(UUID conversation, UUID userId) {
        ConversationData.ConversationDataBuilder builder = ConversationData.builder();
        UUID recipientId = uuidService.combine(conversation, userId);

        builder.id(conversation.toString());

        userRepository
                .findById(recipientId)
                .map(UserData::fromEntity)
                .ifPresent(builder::recipient);

        messageRepository
                .findLastNotDeletedByConversationAndUserId(conversation, userId)
                .map(MessageData::fromEntity)
                .ifPresent(builder::lastMessage);

        long received = messageRepository
                .countNewByConversationAndRecipientId(conversation, userId);

        builder.received(received);

        return builder.build();
    }

    @Override
    public void deleteConversation(String conversationId, String userId) {
        UUID conversation = UUID.fromString(conversationId);
        User user = userRepository
                .findById(UUID.fromString(userId))
                .orElseThrow(() -> throwUserNotFound(userId));
        messageRepository
                .findAllNotDeletedByConversationAndUserId(conversation, user.getId(), Integer.MAX_VALUE, 0)
                .forEach(message -> {
                    if (message.getRecipient().equals(user)) {
                        message.setStatus(MessageStatus.DELIVERED);
                        messageRepository.save(message);
                    }
                    DeletedMessage deletedMessage = new DeletedMessage();
                    deletedMessage.setMessage(message);
                    deletedMessage.setUser(user);
                    deletedMessageRepository.save(deletedMessage);
                });
        user.getConversations().remove(conversation);
        userRepository.save(user);
    }

    @Override
    public void deleteMessage(Long messageId, String userId) {
        User user = userRepository
                .findById(UUID.fromString(userId))
                .orElseThrow(() -> throwUserNotFound(userId));
        Message message = messageRepository
                .findById(messageId)
                .orElseThrow(() -> throwMessageNotFound(messageId));
        if (!message.getRecipient().equals(user) && !message.getSender().equals(user)) {
            throw new OperationRejectedException("Forbidden");
        }
        DeletedMessage deletedMessage = new DeletedMessage();
        deletedMessage.setMessage(message);
        deletedMessage.setUser(user);
        deletedMessageRepository.save(deletedMessage);
    }

    // The return value declared for use in Optional.orElseThrow()
    private RuntimeException throwUserNotFound(String uuid) {
        String error = "User with ID `%s` not found";
        throw new ResourceNotFoundException(String.format(error, uuid));
    }

    private RuntimeException throwConversationNotFound(String uuid) {
        String error = "Conversation with ID `%s` not found";
        throw new ResourceNotFoundException(String.format(error, uuid));
    }

    private RuntimeException throwMessageNotFound(Long id) {
        String error = "Message with ID `%s` not found";
        throw new ResourceNotFoundException(String.format(error, id));
    }

}
