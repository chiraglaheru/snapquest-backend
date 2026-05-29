package com.snapquest.snapquest.controller;

import com.snapquest.snapquest.model.User;
import com.snapquest.snapquest.service.AuthService;

import com.snapquest.snapquest.service.NotificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final NotificationService notificationService;

    public AuthController(
            AuthService authService,
            NotificationService notificationService
    ) {
        this.authService = authService;
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody
            RegisterRequest request
    ) {

        User user =
                authService.register(
                        request.username(),
                        request.email(),
                        request.password()
                );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "User registered successfully",

                        "userId",
                        user.getId(),

                        "username",
                        user.getUsername()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody
            LoginRequest request
    ) {

        String token =
                authService.login(
                        request.username(),
                        request.password()
                );

        User user =
                authService.getUserByUsername(
                        request.username()
                );

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "type", "Bearer",
                        "id", user.getId(),
                        "username",
                        user.getUsername()
                )
        );
    }

    record RegisterRequest(

            @NotBlank
            String username,

            @Email
            @NotBlank
            String email,

            @NotBlank
            String password
    ) {}

    record LoginRequest(

            @NotBlank
            String username,

            @NotBlank
            String password
    ) {}

    @PostMapping("/push-token")
    public ResponseEntity<?> savePushToken(
            @RequestBody Map<String, String> body
    ) {

        authService.savePushToken(
                body.get("username"),
                body.get("token")
        );

        return ResponseEntity.ok(
                Map.of("message", "saved")
        );
    }


}