package com.user_management.service;

import com.user_management.entity.Role;
import com.user_management.entity.User;
import com.user_management.exception.CustomException;
import com.user_management.repository.RoleRepository;
import com.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new CustomException("Username is already taken!");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new CustomException("Email is already registered!");
        }

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            throw new CustomException("Default role USER not found. Please create it first.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(userRole);

        return userRepository.save(user);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new CustomException("Invalid username or password.");
        }

        User user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) { // Replace with password encoder's `matches()` if passwords are encoded
            throw new CustomException("Invalid username or password.");
        }

        return user;
    }
}
