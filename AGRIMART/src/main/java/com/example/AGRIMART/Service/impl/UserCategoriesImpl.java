package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.UserCategoriesDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.UserCategeriesAddResponse;
import com.example.AGRIMART.Dto.response.UserCategoriesGetResponse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Entity.UserCategories;
import com.example.AGRIMART.Repository.UserCategoriesRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.UserCategoriesService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserCategoriesImpl implements UserCategoriesService {

    @Autowired
    private UserCategoriesRepository userCategoriesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public UserCategeriesAddResponse save(UserCategoriesDto userCategoriesDto) {
        // Retrieve username from session
        String username = (String) session.getAttribute("userID");

        if (username == null || username.isEmpty()) {
            UserCategeriesAddResponse response = new UserCategeriesAddResponse();
            response.setMessage("User is not logged in or session expired.");
            response.setStatus("401"); // Unauthorized
            return response;
        }

        // Find user by username
        Optional<User> userOptional = userRepository.findByUserEmail(username);

        if (userOptional.isEmpty()) {
            UserCategeriesAddResponse response = new UserCategeriesAddResponse();
            response.setMessage("User not found for the given username.");
            return response;
        }

        User user = userOptional.get();
        UserCategories userCategories= new UserCategories();
        userCategories.setAboutMe(userCategoriesDto.getAboutMe());
        userCategories.setCategoryOne(UserCategoriesDto.CategoryOne.valueOf(userCategoriesDto.getCategoryOne()));
        userCategories.setCategoryTwo(UserCategoriesDto.CategoryTwo.valueOf(userCategoriesDto.getCategoryTwo()));
        userCategories.setCategoryThree(UserCategoriesDto.CategoryThree.valueOf(userCategoriesDto.getCategoryThree()));
        userCategories.setCategoryFour(UserCategoriesDto.CategoryFour.valueOf(userCategoriesDto.getCategoryFour()));
        userCategories.setCategoryFive(UserCategoriesDto.CategoryFive.valueOf(userCategoriesDto.getCategoryFive()));
        userCategories.setUser(user);

        UserCategeriesAddResponse response = new UserCategeriesAddResponse();
        try {
            UserCategories saveUserCategories = userCategoriesRepository.save(userCategories);
            if (saveUserCategories != null) {
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
    public UserCategoriesGetResponse GetAllUserCategories() {
        UserCategoriesGetResponse response = new UserCategoriesGetResponse();
        try {
            // Fetch all user details
            List<UserCategories> userCategoriesList = userCategoriesRepository.findAll();

            // Map UserDetails entities to a simplified DTO without sensitive data
            List<UserCategoriesDto> userCategoriesDtoList = userCategoriesList.stream()
                    .map(userCategories -> {
                        UserCategoriesDto dto = new UserCategoriesDto();
                        dto.setAboutMe(userCategories.getAboutMe());
                        dto.setCategoryOne(String.valueOf(userCategories.getCategoryOne()));
                        dto.setCategoryTwo(String.valueOf(userCategories.getCategoryTwo()));
                        dto.setCategoryThree(String.valueOf(userCategories.getCategoryThree()));
                        dto.setCategoryFour(String.valueOf(userCategories.getCategoryFour()));
                        dto.setCategoryFive(String.valueOf(userCategories.getCategoryFive()));


                        // Map nested user information without credentials
                        UserDto userDto = new UserDto();
                        userDto.setUserID(userCategories.getUser().getUserID());
                        userDto.setUserEmail(userCategories.getUser().getUserEmail());
                        userDto.setFirstName(userCategories.getUser().getFirstName());
                        userDto.setLastName(userCategories.getUser().getLastName());
                        userDto.setUserType(String.valueOf(userCategories.getUser().getUserType()));



                        dto.setUser(userDto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            response.setUserCategoriesGetResponse(userCategoriesDtoList);
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
