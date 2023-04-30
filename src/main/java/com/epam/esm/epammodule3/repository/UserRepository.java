package com.epam.esm.epammodule3.repository;

import com.epam.esm.epammodule3.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByName(String name);
}
