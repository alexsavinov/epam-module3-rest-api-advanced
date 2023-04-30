package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.model.entity.User;

public interface UserService extends PageableUserService {

    User findById(Long id);

    User findByName(String name);
}
