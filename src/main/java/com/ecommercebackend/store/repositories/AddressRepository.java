package com.ecommercebackend.store.repositories;

import com.ecommercebackend.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}