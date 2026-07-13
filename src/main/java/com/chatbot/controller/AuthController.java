package com.chatbot.controller;

import com.chatbot.model.LoginRequest;
import com.chatbot.model.LoginResponse;
import com.chatbot.model.RegisterRequest;
import com.chatbot.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(value = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    private ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){

        try{
            authService.saveUserDetails(registerRequest);
            return new ResponseEntity<>("User registered successfully", org.springframework.http.HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "User logged in successfully")
    private ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        try{
           LoginResponse loginResponse= authService.loginUser(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
