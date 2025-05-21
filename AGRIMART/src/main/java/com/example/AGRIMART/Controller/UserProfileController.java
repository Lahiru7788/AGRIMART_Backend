package com.example.AGRIMART.Controller;


import com.example.AGRIMART.Dto.UserProfileDto;
import com.example.AGRIMART.Dto.response.UserProfileAddResponse;
import com.example.AGRIMART.Entity.UserProfile;
import com.example.AGRIMART.Repository.UserProfileRepository;
import com.example.AGRIMART.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
    @CrossOrigin(
            origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
            allowCredentials = "true"          // ✅ Allow sending session/cookies
    )
    @RequestMapping("api/user")

    public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private UserProfileRepository userProfileRepository;
    @PostMapping(path = "/userProfile")
    public UserProfileAddResponse save(
            @RequestParam("profilePicture") MultipartFile profileImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] profileImageBytes = profileImage.getBytes();

            UserProfileDto userProfileDto = new UserProfileDto();
            userProfileDto.setProfilePicture(profileImageBytes);

            // Delegate the save operation to the service
            return userProfileService.save(userProfileDto);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


//    @GetMapping("/viewUserProfile")
//
//    public UserProfileGetResponse getAllUserProfiles() {
//        return userProfileService.GetAllUserProfiles();
//
//    }

    @GetMapping("/viewUserProfile")
    public ResponseEntity<byte[]> getUserProfile(@RequestParam int userID) {
        try {
            Optional<UserProfile> image = userProfileRepository.findByUser_UserID(userID);
            if (image.isPresent()) {
                String contentType = detectImageType(image.get().getProfilePicture());

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(image.get().getProfilePicture());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private String detectImageType(byte[] imageBytes) {
        if (imageBytes.length >= 8) {
            if ((imageBytes[0] & 0xFF) == 0x89 && (imageBytes[1] & 0xFF) == 0x50) {
                return "image/png";
            } else if ((imageBytes[0] & 0xFF) == 0xFF && (imageBytes[1] & 0xFF) == 0xD8) {
                return "image/jpeg";
            }
        }
        return "application/octet-stream";
    }

}