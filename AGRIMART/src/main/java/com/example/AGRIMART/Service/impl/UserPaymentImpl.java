package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserPaymentDto;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserPaymentAddResponse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Entity.UserDetails;
import com.example.AGRIMART.Entity.UserPayment;
import com.example.AGRIMART.Repository.UserDetailsRepository;
import com.example.AGRIMART.Repository.UserPaymentRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.UserPaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserPaymentImpl implements UserPaymentService {

    @Autowired
    private UserPaymentRepository userPaymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public UserPaymentAddResponse save(UserPaymentDto userPaymentDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail"); // Assuming "userEmail" stores the username in session

        if (username == null || username.isEmpty()) {
            UserPaymentAddResponse response = new UserPaymentAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            UserPaymentAddResponse response = new UserPaymentAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }
        User user = userOptional.get();
        List<UserPayment> existingUserPaymentList = userPaymentRepository.findByUserPaymentID(userPaymentDto.getUserPaymentID());

        UserPayment userPayment;
        String actionPerformed;

        if (!existingUserPaymentList.isEmpty()) {
            // Update existing user details (assuming we take the first one if multiple exist)
            userPayment = existingUserPaymentList.get(0);
            userPayment.setUserName(userPaymentDto.getUserName());
            userPayment.setCardNumber(userPaymentDto.getCardNumber());
            userPayment.setCvcNumber(userPaymentDto.getCvcNumber());
            userPayment.setExpiryYear(userPaymentDto.getExpiryYear());
            userPayment.setExpiryMonth(userPaymentDto.getExpiryMonth());

            actionPerformed = "updated";
        } else {
            userPayment = new UserPayment();
            userPayment.setUserName(userPaymentDto.getUserName());
            userPayment.setCardNumber(userPaymentDto.getCardNumber());
            userPayment.setCvcNumber(userPaymentDto.getCvcNumber());
            userPayment.setExpiryYear(userPaymentDto.getExpiryYear());
            userPayment.setExpiryMonth(userPaymentDto.getExpiryMonth());
            userPayment.setUser(user);
            actionPerformed = "added";
        }
        UserPaymentAddResponse response = new UserPaymentAddResponse();
        try {
            UserPayment saveUserPayment = userPaymentRepository.save(userPayment);
            if (saveUserPayment != null) {
                response.setMessage("Your details were " + actionPerformed + " successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to " + actionPerformed + " user details.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }
}
