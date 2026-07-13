package com.chatbot.model;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
   // private String refreshToken;
    private String createdAt;
    private String expiredAt;
    private String type;

}
