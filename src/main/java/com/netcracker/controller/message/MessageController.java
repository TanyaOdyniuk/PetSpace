package com.netcracker.controller.message;

import com.netcracker.model.messages.Message;
import com.netcracker.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/{id}")
    public List<Message> getMyMessages(@PathVariable("id") BigInteger id) {
        return messageService.getProfileMessages(id);
    }

    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message){
        return messageService.sendMessage(message);
    }
}
