package com.ecommercebackend.store.repositories;

import com.ecommercebackend.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
