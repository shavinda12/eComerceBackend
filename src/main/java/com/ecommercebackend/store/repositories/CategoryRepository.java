package com.ecommercebackend.store.repositories;

import com.ecommercebackend.store.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}