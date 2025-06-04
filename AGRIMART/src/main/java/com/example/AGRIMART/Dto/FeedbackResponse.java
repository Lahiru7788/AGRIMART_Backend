package com.example.AGRIMART.Dto;



public class FeedbackResponse {
    private boolean success;
    private String message;

    // Constructors
    public FeedbackResponse() {
    }

    public FeedbackResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}