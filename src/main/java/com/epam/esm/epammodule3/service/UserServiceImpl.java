package com.epam.esm.epammodule3.service;

import com.epam.esm.epammodule3.exception.UserNotFoundException;
import com.epam.esm.epammodule3.model.entity.User;
import com.epam.esm.epammodule3.repository.PageableUserRepository;
import com.epam.esm.epammodule3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PageableUserRepository pageableUserRepository;

    @Override
    public User findById(Long id) {
        log.debug("Looking for a user with id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Requested resource not found (id = %s)"
                        .formatted(id)
                ));

        log.info("Retrieved a user with id {}", id);
        return user;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.debug("Retrieving users. Page request: {}", pageable);

        Page<User> users = pageableUserRepository.findAll(pageable);

        log.info("Retrieved {} users of {} total", users.getSize(), users.getTotalElements());
        return users;
    }

    @Override
    public User findByName(String name) {
        log.debug("Looking for a user with name {}", name);
        Optional<User> user = userRepository.findFirstByName(name);

        user.orElseThrow(() -> new UserNotFoundException(
                "Requested resource not found (name = %s)".formatted(name)
        ));

        log.info("Found a user with name {}", name);
        return user.get();
    }
}
