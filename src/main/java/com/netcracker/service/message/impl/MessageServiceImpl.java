package com.netcracker.service.message.impl;

import com.netcracker.dao.manager.query.QueryDescriptor;
import com.netcracker.dao.managerservice.EntityManagerService;
import com.netcracker.model.messages.Message;
import com.netcracker.model.messages.MessageConstants;
import com.netcracker.service.message.MessageService;
import com.netcracker.service.util.PageCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Value("${message.pageCapacity}")
    private String messagePageCapacity;

    private String getMessagesQuery = "SELECT OBJECT_ID \n" +
            "FROM OBJREFERENCE \n" +
            "WHERE ATTRTYPE_ID = " + MessageConstants.MESSAGE_RECEIVER + " \n" +
            "AND REFERENCE = ";

    @Autowired
    EntityManagerService entityManagerService;
    @Autowired
    PageCounterService pageCounterService;


    @Override
    public int getAllMessagePageCount(BigInteger profileId) {
        Integer msgPageCapacity = new Integer(messagePageCapacity);
        return pageCounterService.getPageCount(msgPageCapacity, entityManagerService.getBySqlCount(getMessagesQuery + profileId));
    }

    @Override
    public List<Message> getProfileMessages(BigInteger profileId, int page) {
        String query = this.getMessagesQuery + profileId;
        QueryDescriptor descriptor = new QueryDescriptor();
        descriptor.addSortingDesc(MessageConstants.MESSAGE_DATE, "DESC", true);
        descriptor.addPagingDescriptor(page, Integer.valueOf(messagePageCapacity));
        List<Message> messagesList = entityManagerService.getObjectsBySQL(query, Message.class, descriptor);
        return messagesList;
    }

    @Override
    public Message sendMessage(Message message){
        return entityManagerService.create(message);
    }
}
