package com.github.eybv.messenger.application.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversationData {

    private String id;
    private UserData recipient;
    private MessageData lastMessage;
    private long received;

}
