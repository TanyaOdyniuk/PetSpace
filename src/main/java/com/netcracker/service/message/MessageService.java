package com.netcracker.service.message;

import com.netcracker.model.messages.Message;

import java.math.BigInteger;
import java.util.List;

public interface MessageService {

    int getAllMessagePageCount(BigInteger profileId);
    List<Message> getProfileMessages(BigInteger profileId, int page);

    Message sendMessage(Message message);
}
