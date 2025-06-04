package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.UserBilingDetailsDto;
import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.UserAddResponse;
import com.example.AGRIMART.Dto.response.UserBilingDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Entity.UserBilingDetails;
import com.example.AGRIMART.Entity.UserDetails;
import com.example.AGRIMART.Repository.UserBilingDetailsRepository;
import com.example.AGRIMART.Repository.UserDetailsRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.UserBilingDetailsService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserBilingDetailsImpl implements UserBilingDetailsService {

    @Autowired
    private UserBilingDetailsRepository userBilingDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public UserBilingDetailsAddResponse save(UserBilingDetailsDto userBilingDetailsDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail"); // Assuming "userEmail" stores the username in session

        if (username == null || username.isEmpty()) {
            UserBilingDetailsAddResponse response = new UserBilingDetailsAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            UserBilingDetailsAddResponse response = new UserBilingDetailsAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }
        User user = userOptional.get();
        List<UserBilingDetails> existingUserDetailsList = userBilingDetailsRepository.findByUser_UserID(user.getUserID());

        UserBilingDetails userBilingDetails;
        String actionPerformed;

        if (!existingUserDetailsList.isEmpty()) {
            // Update existing user details (assuming we take the first one if multiple exist)
            userBilingDetails = existingUserDetailsList.get(0);
            userBilingDetails.setAddress(userBilingDetailsDto.getAddress());
            userBilingDetails.setUserFirstName(userBilingDetailsDto.getUserFirstName());
            userBilingDetails.setUserLastName(userBilingDetailsDto.getUserLastName());
            userBilingDetails.setAddedDate(userBilingDetailsDto.getAddedDate());
            userBilingDetails.setCountry(userBilingDetailsDto.getCountry());
            userBilingDetails.setPostalCode(userBilingDetailsDto.getPostalCode());
            userBilingDetails.setMobile(userBilingDetailsDto.getMobile());
            actionPerformed = "updated";
        } else {
            userBilingDetails = new UserBilingDetails();
            userBilingDetails.setAddress(userBilingDetailsDto.getAddress());
            userBilingDetails.setUserFirstName(userBilingDetailsDto.getUserFirstName());
            userBilingDetails.setUserLastName(userBilingDetailsDto.getUserLastName());
            userBilingDetails.setAddedDate(userBilingDetailsDto.getAddedDate());
            userBilingDetails.setCountry(userBilingDetailsDto.getCountry());
            userBilingDetails.setPostalCode(userBilingDetailsDto.getPostalCode());
            userBilingDetails.setMobile(userBilingDetailsDto.getMobile());
            userBilingDetails.setUser(user);
            actionPerformed = "added";
        }
        UserBilingDetailsAddResponse response = new UserBilingDetailsAddResponse();
        try {
            UserBilingDetails saveUserDetails = userBilingDetailsRepository.save(userBilingDetails);
            if (saveUserDetails != null) {
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
