package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/hello")
    private Message hello(){
        return new Message("Hello World");
    }

}
