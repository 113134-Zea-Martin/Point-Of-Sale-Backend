package com.scaffold.template.services.impl;

import com.scaffold.template.entities.UserEntity;
import com.scaffold.template.repositories.UserRepository;
import com.scaffold.template.services.UserService;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
