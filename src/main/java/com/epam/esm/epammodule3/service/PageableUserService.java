package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PageableUserService {

    Page<User> findAll(Pageable pageable);
}
