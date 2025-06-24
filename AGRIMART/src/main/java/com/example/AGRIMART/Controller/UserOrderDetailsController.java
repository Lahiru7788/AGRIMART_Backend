package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserOrderDetailsDto;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Dto.response.UserOrderDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserOrderDetailsGetResponse;
import com.example.AGRIMART.Service.UserDetailsService;
import com.example.AGRIMART.Service.UserOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")
public class UserOrderDetailsController {

    @Autowired
    private UserOrderDetailsService userOrderDetailsService;

    @PostMapping(path = "/userOrderDetails")
    public UserOrderDetailsAddResponse save(@RequestBody UserOrderDetailsDto userOrderDetailsDto){
        return userOrderDetailsService.save(userOrderDetailsDto);
    }


    @GetMapping("/viewUserOrderDetails/{userID}")

    public UserOrderDetailsGetResponse findByUser_UserID(@PathVariable("userID") int userID)  {
        return userOrderDetailsService.getUserOrderDetailsByUserID(userID);

    }

    @GetMapping("/viewAllUserOrderDetails")

    public UserOrderDetailsGetResponse getAllUserDetails() {
        return userOrderDetailsService.GetAllUserOrderDetails();

    }
}
