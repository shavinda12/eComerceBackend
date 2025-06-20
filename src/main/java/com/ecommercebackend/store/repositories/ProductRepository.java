package com.ecommercebackend.store.repositories;

import com.ecommercebackend.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}