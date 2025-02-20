package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/user")

public class UserDetailsController {

    @Autowired
   private UserDetailsService userDetailsService;

    @PostMapping(path = "/userDetails")
    public UserDetailsAddResponse save(@RequestBody UserDetailsDto userDetailsDto){
        return userDetailsService.save(userDetailsDto);
    }

    @GetMapping("/viewUserDetails")

    public UserDetailsGetResponse getAllUserDetails() {
        return userDetailsService.GetAllUserDetails();

    }
}
