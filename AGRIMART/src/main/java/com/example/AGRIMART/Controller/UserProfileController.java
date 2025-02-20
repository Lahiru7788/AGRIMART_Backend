package com.example.AGRIMART.Controller;


import com.example.AGRIMART.Dto.UserProfileDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;
import com.example.AGRIMART.Dto.response.UserProfileAddResponse;
import com.example.AGRIMART.Dto.response.UserProfileGetResponse;
import com.example.AGRIMART.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
    @CrossOrigin
    @RequestMapping("api/user")

    public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping(path = "/userProfile")
    public UserProfileAddResponse save(
            @RequestParam("profilePicture") MultipartFile profileImage,
            @RequestParam("coverPicture") MultipartFile coverImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] profileImageBytes = profileImage.getBytes();
            byte[] coverImageBytes = coverImage.getBytes();

            UserProfileDto userProfileDto = new UserProfileDto();
            userProfileDto.setProfilePicture(profileImageBytes);
            userProfileDto.setCoverPicture(coverImageBytes);

            // Delegate the save operation to the service
            return userProfileService.save(userProfileDto);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/viewUserProfile")

    public UserProfileGetResponse getAllUserProfiles() {
        return userProfileService.GetAllUserProfiles();

    }
}