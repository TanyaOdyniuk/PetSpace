package com.netcracker.model.messages;

import com.netcracker.dao.annotation.Attribute;
import com.netcracker.dao.annotation.ObjectType;
import com.netcracker.dao.annotation.Reference;
import com.netcracker.model.BaseEntity;
import com.netcracker.model.user.Profile;

import java.sql.Timestamp;

@ObjectType(MessageConstants.MESSAGE_TYPE)
public class Message extends BaseEntity{

    @Attribute(MessageConstants.MESSAGE_TEXT)
    private String messageText;
    @Attribute(MessageConstants.MESSAGE_DATE)
    private Timestamp messageDate;
    @Reference(value = MessageConstants.MESSAGE_RECEIVER, isParentChild = 0)
    private Profile messageReceiver;
    @Reference(value = MessageConstants.MESSAGE_SENDER, isParentChild = 0)
    private Profile messageSender;

    public Message() {
    }

    public Message(String messageText, Timestamp messageDate, Profile messageReceiver, Profile messageSender) {
        this.messageText = messageText;
        this.messageDate = messageDate;
        this.messageReceiver = messageReceiver;
        this.messageSender = messageSender;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Timestamp messageDate) {
        this.messageDate = messageDate;
    }

    public Profile getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(Profile messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public Profile getMessageSender() {
        return messageSender;
    }

    public void setMessageSender(Profile messageSender) {
        this.messageSender = messageSender;
    }
}
