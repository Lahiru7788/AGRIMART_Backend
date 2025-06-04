package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserPaymentDto;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserPaymentAddResponse;
import com.example.AGRIMART.Service.UserDetailsService;
import com.example.AGRIMART.Service.UserPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")
public class UserPaymentController {

    @Autowired
    private UserPaymentService userPaymentService;

    @PostMapping(path = "/userPayments")
    public UserPaymentAddResponse save(@RequestBody UserPaymentDto userPaymentDto){
        return userPaymentService.save(userPaymentDto);
    }
}

