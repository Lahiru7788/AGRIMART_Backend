package com.example.AGRIMART.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${feedback.recipient.email}")
    private String recipientEmail;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendFeedbackEmail(String senderEmail, String feedbackMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("New Website Feedback");
        message.setText(
                String.format(
                        "You've received new feedback from: %s\n\nMessage:\n%s",
                        senderEmail,
                        feedbackMessage
                )
        );
        message.setReplyTo(senderEmail);

        mailSender.send(message);
    }

    public void sendNewPassword(String toEmail, String newPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("AGRIMART - Password Reset");
            message.setText(buildPasswordResetEmailContent(newPassword));
            message.setFrom(recipientEmail); // Using the same email from properties

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }

    private String buildPasswordResetEmailContent(String newPassword) {
        return String.format(
                "Dear AGRIMART User,\n\n" +
                        "Your password has been reset successfully.\n\n" +
                        "Your new temporary password is: %s\n\n" +
                        "Please log in with this password and update it immediately for security reasons.\n\n" +
                        "Steps to update your password:\n" +
                        "1. Log in with the temporary password above\n" +
                        "2. Go to your profile/account settings\n" +
                        "3. Change your password to something secure and memorable\n\n" +
                        "If you did not request this password reset, please contact our support team immediately.\n\n" +
                        "Best regards,\n" +
                        "AGRIMART Team",
                newPassword
        );
    }
}