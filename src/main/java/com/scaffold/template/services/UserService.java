package com.scaffold.template.services;

import com.scaffold.template.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserEntity getUserById(Long id);
}
