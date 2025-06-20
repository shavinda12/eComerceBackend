package com.ecommercebackend.store.repositories;

import com.ecommercebackend.store.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
