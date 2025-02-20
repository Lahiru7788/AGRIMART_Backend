package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Entity.UserDetails;
import com.example.AGRIMART.Repository.UserDetailsRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public UserDetailsAddResponse save(UserDetailsDto userDetailsDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail"); // Assuming "userEmail" stores the username in session

        if (username == null || username.isEmpty()) {
            UserDetailsAddResponse response = new UserDetailsAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            UserDetailsAddResponse response = new UserDetailsAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        UserDetails userDetails = new UserDetails();
        userDetails.setAddress(userDetailsDto.getAddress());
        userDetails.setUserFirstName(userDetailsDto.getUserFirstName());
        userDetails.setUserLastName(userDetailsDto.getUserLastName());
        userDetails.setCity(userDetailsDto.getCity());
        userDetails.setCountry(userDetailsDto.getCountry());
        userDetails.setPostalCode(userDetailsDto.getPostalCode());
        userDetails.setMobile(userDetailsDto.getMobile());
        userDetails.setUser(user);

        UserDetailsAddResponse response = new UserDetailsAddResponse();
        try {
            UserDetails saveUserDetails = userDetailsRepository.save(userDetails);
            if (saveUserDetails != null) {
                response.setMessage("Your details were added successfully.");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Failed to add user details.");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500"); // Internal server error
        }

        return response;
    }


//    @Override
//    public UserDetailsGetResponse GetAllUserDetails() {
//        UserDetailsGetResponse response = new UserDetailsGetResponse();
//        try {
//            List<UserDetails> userDetails =userDetailsRepository.findAll();
//            response.setUserDetailsGetResponse(userDetails);
//            response.setStatus("200");
//            response.setMessage("User Details retrieved successfully");
//            response.setResponseCode("1600");
//
//        }catch (Exception e){
//            response.setStatus("500");
//            response.setMessage("Error retrieving User Details: " + e.getMessage());
//            response.setResponseCode("1601");
//
//        }
//
//        return response;
//    }
public UserDetailsGetResponse GetAllUserDetails() {
    UserDetailsGetResponse response = new UserDetailsGetResponse();
    try {
        // Fetch all user details
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();

        // Map UserDetails entities to a simplified DTO without sensitive data
        List<UserDetailsDto> userDetailsDtoList = userDetailsList.stream()
                .map(userDetails -> {
                    UserDetailsDto dto = new UserDetailsDto();
                    dto.setUserDetailsID(userDetails.getUserDetailsID());
                    dto.setUserFirstName(userDetails.getUserFirstName());
                    dto.setUserLastName(userDetails.getUserLastName());
                    dto.setCity(userDetails.getCity());
                    dto.setCountry(userDetails.getCountry());
                    dto.setPostalCode(userDetails.getPostalCode());
                    dto.setMobile(userDetails.getMobile());
                    dto.setAddress(userDetails.getAddress());

                    // Map nested user information without credentials
                    UserDto userDto = new UserDto();
                    userDto.setUserID(userDetails.getUser().getUserID());
                    userDto.setUserEmail(userDetails.getUser().getUserEmail());
                    userDto.setFirstName(userDetails.getUser().getFirstName());
                    userDto.setLastName(userDetails.getUser().getLastName());
                    userDto.setUserType(String.valueOf(userDetails.getUser().getUserType()));



                    dto.setUser(userDto);
                    return dto;
                })
                .collect(Collectors.toList());

        response.setUserDetailsGetResponse(userDetailsDtoList);
        response.setStatus("200");
        response.setMessage("User Details retrieved successfully");
        response.setResponseCode("1600");

    } catch (Exception e) {
        response.setStatus("500");
        response.setMessage("Error retrieving User Details: " + e.getMessage());
        response.setResponseCode("1601");
    }

    return response;
}

}
