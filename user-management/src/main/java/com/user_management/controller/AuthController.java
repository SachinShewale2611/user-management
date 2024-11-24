package com.user_management.controller;

import com.user_management.config.JwtUtil;
import com.user_management.dto.JwtResponse;
import com.user_management.dto.LoginRequest;
import com.user_management.dto.SignupRequest;
import com.user_management.entity.User;

import com.user_management.repository.RoleRepository;
import com.user_management.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthController(JwtUtil jwtUtil, UserService userService,
                          PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(user.getUsername());
        return new JwtResponse(token);
    }

    @PostMapping("/signup")
    public JwtResponse registerUser(@RequestBody SignupRequest request) {
        User user = userService.register(request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail());
        String token = jwtUtil.generateToken(user.getUsername());
        return new JwtResponse(token);
    }

}
