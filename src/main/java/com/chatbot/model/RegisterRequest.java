package com.chatbot.model;


import com.chatbot.util.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "register_data")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequest {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "user_name",nullable = false)
    private String username;
    @Column(name = "email",nullable = false)
    @Size(max = 100,min = 5)
    private String email;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name="phone_no")
    private String phoneNo;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;


}

