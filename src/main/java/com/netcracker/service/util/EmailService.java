package com.netcracker.service.util;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private MailSender mailSender;

    public void setMailSender(MailSender sender){
        this.mailSender = sender;
    }

    public void sendMail(String from, String to, String subject, String msg){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }
}
