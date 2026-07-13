package com.chatbot.exception;

public class ResourceNotFoundException extends Exception{
    private String message;
    private String statusCode;

    public ResourceNotFoundException(String message, String statusCode) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
