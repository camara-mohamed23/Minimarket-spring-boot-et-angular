package com.example.demo.common;

import java.time.Instant;

public class ApiError {
    private int status;
    private String message;
    private Instant timestamp;

    public ApiError() {
        this.timestamp = Instant.now();
    }

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now();
    }

    // ✅ nouveau constructeur pour compatibilité
    public ApiError(String message) {
        this.status = 500; // ou 400 selon ton besoin
        this.message = message;
        this.timestamp = Instant.now();
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
