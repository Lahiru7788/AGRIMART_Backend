package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")

public class UserDetailsController {

    @Autowired
   private UserDetailsService userDetailsService;

    @PostMapping(path = "/userDetails")
    public UserDetailsAddResponse save(@RequestBody UserDetailsDto userDetailsDto){
        return userDetailsService.save(userDetailsDto);
    }


    @GetMapping("/viewUserDetails/{userID}")

    public UserDetailsGetResponse findByUser_UserID(@PathVariable("userID") int userID)  {
        return userDetailsService.getUserDetailsByUserID(userID);

    }

    @GetMapping("/viewAllUserDetails")

    public UserDetailsGetResponse getAllUserDetails() {
        return userDetailsService.GetAllUserDetails();

    }
}
