package com.ecommercebackend.store.controller;

import com.ecommercebackend.store.dtos.OrderDto;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.mappers.OrderMapper;
import com.ecommercebackend.store.repositories.OrderRepository;
import com.ecommercebackend.store.service.AuthService;
import com.ecommercebackend.store.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrders());
    }
}
