package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.CredentialDto;
import com.example.AGRIMART.Dto.LoginDto;
import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.LoginResponse;
import com.example.AGRIMART.Entity.Credentials;
import com.example.AGRIMART.Repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class LoginService {

    @Autowired
    private CredentialRepository credentialRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public LoginResponse authenticateUser(LoginDto loginDto) {
        LoginResponse loginResponse = new LoginResponse();
        Credentials credentials = credentialRepository.findByUserEmail(loginDto.getUserEmail());

        // Debugging
        if (credentials == null) {
            System.out.println("User not found");
            loginResponse.setStatus("400");
            loginResponse.setMessage("Invalid username or user password");
            loginResponse.setCode("8001");

            return loginResponse;
        }


        // Password checking logic
        if (credentials != null &&
                bCryptPasswordEncoder.matches(loginDto.getUserPassword(), credentials.getUserPassword())) {

            // Create the DTO for the logged-in user's credentials
            CredentialDto credentialDto = new CredentialDto();
            credentialDto.setUserEmail(credentials.getUserEmail());

            // Create the UserDto for the associated user
            UserDto userDto = new UserDto();
            userDto.setUserID(credentials.getUser().getUserID());
            userDto.setUserEmail(credentials.getUser().getUserEmail());
            userDto.setFirstName(credentials.getUser().getFirstName());
            userDto.setLastName(credentials.getUser().getLastName());
            userDto.setUserType(String.valueOf(credentials.getUser().getUserType()));

            credentialDto.setUser(userDto);

            // Set the response with the single logged-in user's details
            loginResponse.setCredentials(Collections.singletonList(credentialDto)); // Wrap the DTO in a single-element list
            loginResponse.setStatus("200");
            loginResponse.setMessage("Login Success");
            loginResponse.setCode("8000");

        } else {
            // Set the response for invalid credentials
            loginResponse.setStatus("400");
            loginResponse.setMessage("Invalid username or user password");
            loginResponse.setCode("8001");
        }

        return loginResponse;


    }
}
