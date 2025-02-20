package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.UserProfileDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Dto.response.UserProfileAddResponse;
import com.example.AGRIMART.Dto.response.UserProfileGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProductImage;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Entity.UserDetails;
import com.example.AGRIMART.Entity.UserProfile;
import com.example.AGRIMART.Repository.UserDetailsRepository;
import com.example.AGRIMART.Repository.UserProfileRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.UserDetailsService;
import com.example.AGRIMART.Service.UserProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileImpl implements UserProfileService {

        @Autowired
        private UserProfileRepository userProfileRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private HttpSession session;

        @Override
        public UserProfileAddResponse save(UserProfileDto userProfileDto) {
            // Retrieve username from session
            String username = (String) session.getAttribute("userEmail"); // Assuming "userEmail" stores the username in session

            if (username == null || username.isEmpty()) {
                UserProfileAddResponse response = new UserProfileAddResponse();
                response.setMessage("User is not logged in or session expired.");
                response.setStatus("401"); // Unauthorized
                return response;
            }

            // Find user by username
            Optional<User> userOptional = userRepository.findByUserEmail(username);

            if (userOptional.isEmpty()) {
                UserProfileAddResponse response = new UserProfileAddResponse();
                response.setMessage("User not found for the given username.");
                return response;
            }

            User user = userOptional.get();
            UserProfile userProfile = new UserProfile();
            userProfile.setProfilePicture(userProfileDto.getProfilePicture());
            userProfile.setCoverPicture(userProfileDto.getCoverPicture());

            userProfile.setUser(user);

            UserProfileAddResponse response = new UserProfileAddResponse();
            try {
                UserProfile saveUserProfile = userProfileRepository.save(userProfile);
                if (saveUserProfile != null) {
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


    public UserProfileGetResponse GetAllUserProfiles() {
        UserProfileGetResponse response = new UserProfileGetResponse();
        try {
            // Fetch all user details
            List<UserProfile> userProfileList = userProfileRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<UserProfileDto> userProfileDtoList = userProfileList.stream()
                    .map(userProfile -> {
                        UserProfileDto dto = new UserProfileDto();
                        dto.setProfileID(userProfile.getProfileID());
                        dto.setProfilePicture(userProfile.getProfilePicture());
                        dto.setCoverPicture(userProfile.getCoverPicture());


                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(userProfile.getUser().getUserID());
                        userDto.setUserEmail(userProfile.getUser().getUserEmail());
                        userDto.setFirstName(userProfile.getUser().getFirstName());
                        userDto.setLastName(userProfile.getUser().getLastName());
                        userDto.setUserType(String.valueOf(userProfile.getUser().getUserType()));


                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setUserProfileGetResponse(userProfileDtoList);
            response.setStatus("200");
            response.setMessage("Profile images retrieved successfully");
            response.setResponseCode("1600");

        } catch (Exception e) {
            response.setStatus("500");
            response.setMessage("Error retrieving product Details: " + e.getMessage());
            response.setResponseCode("1601");
        }

        return response;
    }



}
