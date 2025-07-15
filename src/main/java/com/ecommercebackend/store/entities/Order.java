package com.ecommercebackend.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @OneToMany(mappedBy = "order_id", cascade = CascadeType.PERSIST)

    private List<OrderItem> items=new ArrayList<>();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at",insertable = false,updatable = false )
    private LocalDateTime created_at;

    @Column(name = "total_price",nullable = false)
    private BigDecimal total_price;

    public static Order fromCart(Cart cart,User customer){
        var order=new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setTotal_price(cart.getTotalPrice());
        cart.getItems().forEach(cartItem -> {
            var orderItem=new OrderItem(order,cartItem.getProduct(),cartItem.getQuantity());
            order.items.add(orderItem);
        });
        return order;
    }

    public void addOrderItem(OrderItem orderItem){
        items.add(orderItem);
        orderItem.setOrder_id(this);
    }
}
