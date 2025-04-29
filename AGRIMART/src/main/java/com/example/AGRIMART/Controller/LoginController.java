package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.LoginDto;
import com.example.AGRIMART.Dto.response.LoginResponse;
import com.example.AGRIMART.Dto.response.LogoutResponse;
import com.example.AGRIMART.Service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//import javax.servlet.http.HttpSession;

@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
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
    @PostMapping(value = "/logout")
    public LogoutResponse logout(HttpSession session) {
        // Get the logout response from service
        LogoutResponse response = loginService.logoutUser();

        // Invalidate the session to clear all session attributes
        session.invalidate();

        return response;
    }

}
