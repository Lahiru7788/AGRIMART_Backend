package com.example.AGRIMART.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class FeedbackRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Message is required")
    private String message;

    // Constructors
    public FeedbackRequest() {
    }

    public FeedbackRequest(String email, String message) {
        this.email = email;
        this.message = message;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FeedbackRequest{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

// Response DTO


