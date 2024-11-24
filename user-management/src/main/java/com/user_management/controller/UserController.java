package com.user_management.controller;

import com.user_management.dto.ForgotPasswordRequest;
import com.user_management.dto.ResetPasswordRequest;
import com.user_management.util.EmailUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final EmailUtil emailUtil;

    public UserController(EmailUtil emailUtil) {
        this.emailUtil = emailUtil;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            // Placeholder logic to generate a token
            String resetToken = "RESET-TOKEN"; // Implement token generation logic
            emailUtil.sendEmail(request.getEmail(), "Reset Password", "Token: " + resetToken);
            return ResponseEntity.ok("Reset password token sent successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Failed to send email.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        // Placeholder logic to validate token and update password
        if (!"RESET-TOKEN".equals(request.getToken())) {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
        // Update user password in the database
        return ResponseEntity.ok("Password reset successfully.");
    }

    @GetMapping("/")
    public String index() {
        return "Hello World!";
    }
}
