package com.ecommercebackend.store.controller;


import lombok.Locked;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "index.html";
    }
}
