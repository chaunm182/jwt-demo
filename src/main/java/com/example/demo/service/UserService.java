package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    Optional<User> findById(Long id);
}
