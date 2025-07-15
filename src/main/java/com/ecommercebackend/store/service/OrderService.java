package com.ecommercebackend.store.service;

import com.ecommercebackend.store.dtos.OrderDto;
import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.mappers.OrderMapper;
import com.ecommercebackend.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders(){
        var user=authService.getCurrentUser();
        List<Order> orders= orderRepository.getAllByCustomer(user);
        return orders.stream().map(order->orderMapper.toDto(order)).toList();
    }
}
