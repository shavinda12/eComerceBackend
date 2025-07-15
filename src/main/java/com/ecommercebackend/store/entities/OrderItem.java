package com.ecommercebackend.store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order_id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "unit_price")
    private BigDecimal unit_price;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name = "total_price")
    private BigDecimal total_price;

    public OrderItem(Order order, Product product, Integer quantity) {
        this.order_id = order;
        this.product = product;
        this.quantity = quantity;
        this.unit_price = product.getPrice();
        this.total_price = unit_price.multiply(new BigDecimal(quantity));

    }
}
