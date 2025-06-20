package com.ecommercebackend.store.repositories;

import com.ecommercebackend.store.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}