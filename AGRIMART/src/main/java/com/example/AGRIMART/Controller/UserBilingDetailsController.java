package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.UserBilingDetailsDto;
import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.response.UserBilingDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Service.UserBilingDetailsService;
import com.example.AGRIMART.Service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")
public class UserBilingDetailsController {

    @Autowired
    private UserBilingDetailsService userBilingDetailsService;

    @PostMapping(path = "/userBillingDetails")
    public UserBilingDetailsAddResponse save(@RequestBody UserBilingDetailsDto userBilingDetailsDto){
        return userBilingDetailsService.save(userBilingDetailsDto);
    }


}
