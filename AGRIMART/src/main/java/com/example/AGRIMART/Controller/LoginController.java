package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.LoginDto;
import com.example.AGRIMART.Dto.response.LoginResponse;
import com.example.AGRIMART.Service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import javax.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping(value = "/login")
    public LoginResponse login(@RequestBody LoginDto loginDto, HttpSession session) {
        LoginResponse response = loginService.authenticateUser(loginDto);

        if ("200".equals(response.getStatus())) {
            // Save user data in session on successful login
            session.setAttribute("userEmail", loginDto.getUserEmail());
            session.setAttribute("userPassword", loginDto.getUserPassword());
            session.setAttribute("userID",loginDto.getUserID());
        }

        return response;
    }


}
