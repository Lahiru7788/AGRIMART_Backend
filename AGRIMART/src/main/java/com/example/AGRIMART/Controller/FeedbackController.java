package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.FeedbackRequest;
import com.example.AGRIMART.Dto.FeedbackResponse;
import com.example.AGRIMART.Service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "${app.cors.allowed-origins}")  // Configure this in application.properties
public class FeedbackController {

    private final EmailService emailService;

    @Autowired
    public FeedbackController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        try {
            emailService.sendFeedbackEmail(request.getEmail(), request.getMessage());
            return ResponseEntity.ok(new FeedbackResponse(true, "Feedback received successfully"));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new FeedbackResponse(false, "Failed to process feedback: " + e.getMessage()));
        }
    }
}
