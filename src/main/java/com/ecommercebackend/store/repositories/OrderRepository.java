package com.ecommercebackend.store.repositories;

import com.ecommercebackend.store.entities.Order;
import com.ecommercebackend.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o WHERE o.customer=:customer")
    List<Order> getAllByCustomer(@Param("customer") User customer);
}
