package com.netcracker.service.message;

import com.netcracker.model.messages.Message;

import java.math.BigInteger;
import java.util.List;

public interface MessageService {

    List<Message> getProfileMessages(BigInteger profileId);

    Message sendMessage(Message message);
}
