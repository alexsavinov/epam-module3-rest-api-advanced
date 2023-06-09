package com.epam.esm.epammodule3.repository;

import com.epam.esm.epammodule3.model.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageableUserRepository extends PagingAndSortingRepository<User, Long> {
}
