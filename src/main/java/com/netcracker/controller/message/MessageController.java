package com.netcracker.controller.message;

import com.netcracker.model.messages.Message;
import com.netcracker.service.message.MessageService;
import com.netcracker.service.util.RestResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/{id}/{page}")
    public RestResponsePage<Message> getMyMessages(@PathVariable("id") BigInteger profileId, @PathVariable("page") int page) {
        Integer count = messageService.getAllMessagePageCount(profileId);
        List<Message> messagesList =  messageService.getProfileMessages(profileId, page);
        return new RestResponsePage<>(messagesList, null, count);
    }

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message){
        return messageService.sendMessage(message);
    }
}
