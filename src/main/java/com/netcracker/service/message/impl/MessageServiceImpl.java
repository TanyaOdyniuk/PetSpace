package com.netcracker.service.message.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.messages.Message;
import com.netcracker.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    EntityManagerService entityManagerService;

    @Override
    public List<Message> getProfileMessages(BigInteger profileId) {
        String query = "SELECT OBJECT_ID \n" +
                "FROM OBJREFERENCE \n" +
                "WHERE ATTRTYPE_ID = 31 /*RECEIVER*/\n" +
                "AND REFERENCE = " + profileId /*+ " \n" +
                "ORDER BY OBJECT_ID DESC"*/;
        List<Message> messagesList = entityManagerService.getObjectsBySQL(query, Message.class, new QueryDescriptor());
        return messagesList;
    }

    @Override
    public Message sendMessage(Message message){
        return entityManagerService.create(message);
    }
}
