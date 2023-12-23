package com.example.web.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizationException extends RuntimeException {

    private final ObjectMapper objectMapper;

    public AuthorizationException(ObjectMapper objectMapper, String message) {
        super(message);
        this.objectMapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
