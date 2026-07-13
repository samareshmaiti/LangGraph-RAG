package com.chatbot.service;

import com.chatbot.exception.UserAlreadyExistsException;
import com.chatbot.model.LoginRequest;
import com.chatbot.model.LoginResponse;
import com.chatbot.model.RegisterRequest;
import com.chatbot.repository.UserRepository;
import com.chatbot.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @Value("${app.jwt.expiration}")
    private Long expiration;

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    private final UserRepository userRepository;
    private final JwtUtils jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public boolean saveUserDetails(RegisterRequest registerRequest) {
        logger.info("Starting user registration for: " + registerRequest.getUsername());
        try {
            // Check if user already exists
            logger.info("Checking if user exists: " + registerRequest.getUsername());
            boolean existsByUsername = userRepository.existsByUsername(registerRequest.getUsername());
            boolean existsByUserEmail = userRepository.existsByEmail(registerRequest.getEmail());
            logger.info("User exists by username: " + existsByUsername + ", by email: " + existsByUserEmail);
            if (existsByUsername || existsByUserEmail) {
                logger.warning("User already exists: " + registerRequest.getUsername());
                throw new UserAlreadyExistsException("User already exists", HttpStatus.BAD_REQUEST.toString());
            }
            RegisterRequest request = RegisterRequest.builder()
                    .name(registerRequest.getName())
                    .username(registerRequest.getUsername())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .email(registerRequest.getEmail())
                    .phoneNo(registerRequest.getPhoneNo())
                    .role(registerRequest.getRole())
                    .build();
            RegisterRequest savedUser = userRepository.save(request);
            logger.info("User registered successfully: " + savedUser.getUsername());
            return ((!savedUser.getEmail().isEmpty() && !savedUser.getUsername().isEmpty()));
        } catch (Exception e) {
            logger.severe("Error during user registration: " + e.getMessage());
            throw new RuntimeException("Error during user registration", e);

        }

    }
    public LoginResponse loginUser(LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        logger.info("Attempting login for user: " + username);

            // Retrieve user from database
            Optional<RegisterRequest> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

            if (userOptional.isEmpty()) {
                logger.warning("User not found: " + username);
                throw new RuntimeException("User not found");
            }

            RegisterRequest user = userOptional.get();

            // Validate password
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
        } catch (Exception e) {
            logger.severe("Authentication failed: " + e.getClass().getName());
            logger.severe("Message: " + e.getMessage());
            throw e;
        }
            // Generate JWT token with username and roles
            String roles = user.getRole() != null ? user.getRole().toString() : "";
            String email=user.getEmail() !=null?user.getEmail().toString():"";
            // String name=user.getName() !=null?user.getName().toString():"";
            String jwtToken = jwtService.generateJwtToken(username, email,roles);

            logger.info("Login successful for user: " + username);

            // Build and return LoginResponse
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(jwtToken);
            loginResponse.setType("Bearer");
            loginResponse.setCreatedAt(String.valueOf(System.currentTimeMillis()));
            loginResponse.setExpiredAt(String.valueOf(System.currentTimeMillis() + expiration));

            return loginResponse;
        }
    }



