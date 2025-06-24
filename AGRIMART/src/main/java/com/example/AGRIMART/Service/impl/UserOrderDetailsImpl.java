package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.UserOrderDetailsDto;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Dto.response.UserOrderDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserOrderDetailsGetResponse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Entity.UserDetails;
import com.example.AGRIMART.Entity.UserOrderDetails;
import com.example.AGRIMART.Repository.UserDetailsRepository;
import com.example.AGRIMART.Repository.UserOrderDetailsRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.UserOrderDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserOrderDetailsImpl implements UserOrderDetailsService {

    @Autowired
    private UserOrderDetailsRepository userOrderDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public UserOrderDetailsAddResponse save(UserOrderDetailsDto userOrderDetailsDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userEmail"); // Assuming "userEmail" stores the username in session

        if (username == null || username.isEmpty()) {
            UserOrderDetailsAddResponse response = new UserOrderDetailsAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            UserOrderDetailsAddResponse response = new UserOrderDetailsAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }
        User user = userOptional.get();
        List<UserOrderDetails> existingUserDetailsList = userOrderDetailsRepository.findByUserOrderDetailsID(userOrderDetailsDto.getUserOrderDetailsID());

        UserOrderDetails userOrderDetails;
        String actionPerformed;

        if (!existingUserDetailsList.isEmpty()) {
            // Update existing user details (assuming we take the first one if multiple exist)
            userOrderDetails = existingUserDetailsList.get(0);
            userOrderDetails.setOrderNumber(userOrderDetailsDto.getOrderNumber());
            userOrderDetails.setAddedDate(userOrderDetailsDto.getAddedDate());
            userOrderDetails.setUserType(userOrderDetailsDto.getUserType());
            userOrderDetails.setQuantity(userOrderDetailsDto.getQuantity());
            userOrderDetails.setPrice(userOrderDetailsDto.getPrice());
            actionPerformed = "updated";
        } else {
            userOrderDetails = new UserOrderDetails();
            userOrderDetails.setOrderNumber(userOrderDetailsDto.getOrderNumber());
            userOrderDetails.setAddedDate(userOrderDetailsDto.getAddedDate());
            userOrderDetails.setUserType(userOrderDetailsDto.getUserType());
            userOrderDetails.setQuantity(userOrderDetailsDto.getQuantity());
            userOrderDetails.setPrice(userOrderDetailsDto.getPrice());
            userOrderDetails.setUser(user);
            actionPerformed = "added";
        }
        UserOrderDetailsAddResponse response = new UserOrderDetailsAddResponse();
        try {
            UserOrderDetails saveUserOrderDetails = userOrderDetailsRepository.save(userOrderDetails);
            if (saveUserOrderDetails != null) {
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
//public UserDetailsGetResponse GetAllUserDetails() {
//    UserDetailsGetResponse response = new UserDetailsGetResponse();
//    try {
//        // Fetch all user details
//        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
//
//        // Map UserDetails entities to a simplified DTO without sensitive data
//        List<UserDetailsDto> userDetailsDtoList = userDetailsList.stream()
//                .map(userDetails -> {
//                    UserDetailsDto dto = new UserDetailsDto();
//                    dto.setUserDetailsID(userDetails.getUserDetailsID());
//                    dto.setUserFirstName(userDetails.getUserFirstName());
//                    dto.setUserLastName(userDetails.getUserLastName());
//                    dto.setCity(userDetails.getCity());
//                    dto.setCountry(userDetails.getCountry());
//                    dto.setPostalCode(userDetails.getPostalCode());
//                    dto.setMobile(userDetails.getMobile());
//                    dto.setAddress(userDetails.getAddress());
//
//                    // Map nested user information without credentials
//                    UserDto userDto = new UserDto();
//                    userDto.setUserID(userDetails.getUser().getUserID());
//                    userDto.setUserEmail(userDetails.getUser().getUserEmail());
//                    userDto.setFirstName(userDetails.getUser().getFirstName());
//                    userDto.setLastName(userDetails.getUser().getLastName());
//                    userDto.setUserType(String.valueOf(userDetails.getUser().getUserType()));
//
//
//
//                    dto.setUser(userDto);
//                    return dto;
//                })
//                .collect(Collectors.toList());
//
//        response.setUserDetailsGetResponse(userDetailsDtoList);
//        response.setStatus("200");
//        response.setMessage("User Details retrieved successfully");
//        response.setResponseCode("1600");
//
//    } catch (Exception e) {
//        response.setStatus("500");
//        response.setMessage("Error retrieving User Details: " + e.getMessage());
//        response.setResponseCode("1601");
//    }

//    return response;
//}

    public UserOrderDetailsGetResponse getUserOrderDetailsByUserID(int userID) {
        UserOrderDetailsGetResponse response = new UserOrderDetailsGetResponse();
        try {
            // Use the correct repository method that follows the entity relationship
            List<UserOrderDetails> userOrderDetailsList = userOrderDetailsRepository.findByUser_UserID(userID);

            if (userOrderDetailsList.isEmpty()) {
                response.setStatus("404");
                response.setMessage("No user details found: " + userID);
                response.setResponseCode("1602");
                return response;
            }
            // Map FarmerOffer entities to DTOs
            List<UserOrderDetailsDto> userOrderDetailsDtoList = userOrderDetailsList.stream()
                    .map(userOrderDetails -> {
                        UserOrderDetailsDto dto = new UserOrderDetailsDto();
                        dto.setUserOrderDetailsID(userOrderDetails.getUserOrderDetailsID());
                        dto.setOrderNumber(userOrderDetails.getOrderNumber());
                        dto.setAddedDate(userOrderDetails.getAddedDate());
                        dto.setUserType(userOrderDetails.getUserType());
                        dto.setQuantity(userOrderDetails.getQuantity());
                        dto.setPrice(userOrderDetails.getPrice());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(userOrderDetails.getUser().getUserID());
                        userDto.setUserEmail(userOrderDetails.getUser().getUserEmail());
                        userDto.setFirstName(userOrderDetails.getUser().getFirstName());
                        userDto.setLastName(userOrderDetails.getUser().getLastName());
                        userDto.setUserType(String.valueOf(userOrderDetails.getUser().getUserType()));

                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setUserOrderDetailsGetResponse(userOrderDetailsDtoList);
            response.setStatus("200");
            response.setMessage("User Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving user details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }

    public UserOrderDetailsGetResponse GetAllUserOrderDetails() {
        UserOrderDetailsGetResponse response = new UserOrderDetailsGetResponse();
        try {

            List<UserOrderDetails> userOrderDetailsList = userOrderDetailsRepository.findAll();

            // Map FarmerOffer entities to DTOs
            List<UserOrderDetailsDto> userOrderDetailsDtoList = userOrderDetailsList.stream()
                    .map(userOrderDetails -> {
                        UserOrderDetailsDto dto = new UserOrderDetailsDto();
                        dto.setUserOrderDetailsID(userOrderDetails.getUserOrderDetailsID());
                        dto.setOrderNumber(userOrderDetails.getOrderNumber());
                        dto.setAddedDate(userOrderDetails.getAddedDate());
                        dto.setUserType(userOrderDetails.getUserType());
                        dto.setQuantity(userOrderDetails.getQuantity());
                        dto.setPrice(userOrderDetails.getPrice());

                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(userOrderDetails.getUser().getUserID());
                        userDto.setUserEmail(userOrderDetails.getUser().getUserEmail());
                        userDto.setFirstName(userOrderDetails.getUser().getFirstName());
                        userDto.setLastName(userOrderDetails.getUser().getLastName());
                        userDto.setUserType(String.valueOf(userOrderDetails.getUser().getUserType()));

                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setUserOrderDetailsGetResponse(userOrderDetailsDtoList);
            response.setStatus("200");
            response.setMessage("User Details retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving user details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }
}
