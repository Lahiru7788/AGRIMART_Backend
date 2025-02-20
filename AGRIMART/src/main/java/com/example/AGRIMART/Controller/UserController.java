package com.example.AGRIMART.Controller;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.UserAddResponse;
import com.example.AGRIMART.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/registration")
    public UserAddResponse save(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }





}
