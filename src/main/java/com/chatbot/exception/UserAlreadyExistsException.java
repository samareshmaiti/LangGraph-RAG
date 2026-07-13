package com.chatbot.exception;

public class UserAlreadyExistsException extends Exception{
    private String message;
    private String statusCode;

    public UserAlreadyExistsException(String message,String statusCode) {
        this.message=message;
        this.statusCode=statusCode;

    }
}
