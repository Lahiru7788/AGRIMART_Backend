//package com.example.AGRIMART.Service.impl;
//
//import com.example.AGRIMART.Dto.CredentialDto;
//import com.example.AGRIMART.Dto.response.CredentialAddResponse;
//import com.example.AGRIMART.Entity.Credentials;
//import com.example.AGRIMART.Entity.User;
//import com.example.AGRIMART.Repository.CredentialRepository;
//import com.example.AGRIMART.Repository.UserRepository;
//import com.example.AGRIMART.Service.CredentialService;
//import jakarta.transaction.Transactional;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CredentialImpl implements CredentialService {
//
//    @Autowired
//    private CredentialRepository credentialRepository;
//    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//    @Autowired
//    private UserRepository userRepository;
//
//
//    @Autowired
//    ModelMapper modelMapper;
//
////    @Override
////    public CredentialAddResponse save(CredentialDto credentialDto) {
////
////
////
////        String encodePassword = bCryptPasswordEncoder.encode(credentialDto.getUserPassword());
////        credentialDto.setUserPassword(encodePassword);
////
////        Credentials credentials = modelMapper.map(credentialDto, Credentials.class);
////
////        //set response
////        CredentialAddResponse response =new CredentialAddResponse();
////        try {
////            Credentials saveNewCredentials = credentialRepository.save(credentials);
////            if (saveNewCredentials != null) {
////                response.setCredentials(credentials);
////                response.setMessage("New Credentials added Successful");
////                response.setStatus("200");
////                response.setResponseCode("1000");
////            } else {
////                response.setMessage("New Credentials added Failure");
////                response.setStatus("400");
////            }
////        } catch (Exception e) {
////            // Handle the exception
////            response.setMessage("Error: " + e.getMessage()); // Customize the error message as needed
////            response.setStatus("500"); // Internal server error status
////        }
//@Override
//@Transactional
//public CredentialAddResponse save(CredentialDto credentialDto) {
//    CredentialAddResponse response = new CredentialAddResponse();
//
//    try {
//        // Check if user exists or create a new one
//        User user = userRepository.findByUserEmail(credentialDto.getUserEmail())
//                .orElseGet(() -> {
//                     User newUser = new User();
//                    newUser.setUserEmail(credentialDto.getUserEmail());
//
//                    newUser.setLastName(credentialDto.getLastName());
//                    newUser.setUserType(credentialDto.getUserType());
//                    newUser.setActive(true);
//                    return userRepository.save(newUser);
//                });
//
//        // Encode password and save credentials
//        String encodedPassword = bCryptPasswordEncoder.encode(credentialDto.getUserPassword());
//        credentialDto.setUserPassword(encodedPassword);
//
//        Credentials credentials = modelMapper.map(credentialDto, Credentials.class);
//        credentials.setUser(user);
//
//        Credentials savedCredentials = credentialRepository.save(credentials);
//
//        // Populate response
//        response.setCredentials(savedCredentials);
//        response.setMessage("New Credentials added successfully");
//        response.setStatus("200");
//        response.setResponseCode("1000");
//    } catch (Exception e) {
//        response.setMessage("Error: " + e.getMessage());
//        response.setStatus("500");
//    }
//
//
//
//
//    return response;
//
//

package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.CredentialDto;
import com.example.AGRIMART.Dto.response.CredentialAddResponse;
import com.example.AGRIMART.Dto.request.ForgotPasswordRequest;
import com.example.AGRIMART.Dto.request.UpdatePasswordRequest;
import com.example.AGRIMART.Dto.response.ForgotPasswordResponse;
import com.example.AGRIMART.Dto.response.UpdatePasswordResponse;
import com.example.AGRIMART.Entity.Credentials;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.CredentialRepository;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.CredentialService;
import com.example.AGRIMART.Service.EmailService;
import com.example.AGRIMART.Util.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialImpl implements CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService; // Using your existing EmailService

    @Autowired
    private PasswordGenerator passwordGenerator;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public CredentialAddResponse save(CredentialDto credentialDto) {
        Optional<User> userOptional = userRepository.findByUserEmail(credentialDto.getUserEmail());

        if (userOptional.isEmpty()) {
            CredentialAddResponse response = new CredentialAddResponse();
            response.setMessage("User not found for the given email.");
            return response;
        }

        String encodePassword = bCryptPasswordEncoder.encode(credentialDto.getUserPassword());
        credentialDto.setUserPassword(encodePassword);

        User user = userOptional.get();
        Credentials credentials = new Credentials();
        credentials.setUserEmail(credentialDto.getUserEmail());
        credentials.setUserPassword(credentialDto.getUserPassword());
        credentials.setUser(user);

        CredentialAddResponse response = new CredentialAddResponse();
        try {
            Credentials saveCredentials = credentialRepository.save(credentials);
            if (saveCredentials != null) {
                response.setCredentials(credentials);
                response.setMessage("Your Credentials added Successful");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("Your Credentials added Failure");
                response.setStatus("400");
            }
        } catch (Exception e) {
            response.setMessage("Error: " + e.getMessage());
            response.setStatus("500");
        }

        return response;
    }

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        ForgotPasswordResponse response = new ForgotPasswordResponse();

        try {
            // Check if user exists in credentials table
            Credentials existingCredentials = credentialRepository.findByUserEmail(request.getUserEmail());

            if (existingCredentials == null) {
                response.setMessage("User not found with the provided email address.");
                response.setStatus("404");
                response.setResponseCode("2001");
                return response;
            }

            // Generate new password
            String newPassword = passwordGenerator.generatePassword(10);
            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);

            // Update the existing credentials with new password
            existingCredentials.setUserPassword(encodedPassword);
            credentialRepository.save(existingCredentials);

            // Send email with new password using your existing EmailService
            emailService.sendNewPassword(request.getUserEmail(), newPassword);

            response.setMessage("A new password has been sent to your email address.");
            response.setStatus("200");
            response.setResponseCode("1000");

        } catch (Exception e) {
            response.setMessage("Failed to process password reset: " + e.getMessage());
            response.setStatus("500");
            response.setResponseCode("2002");
        }

        return response;
    }

    @Override
    public UpdatePasswordResponse updatePassword(UpdatePasswordRequest request) {
        UpdatePasswordResponse response = new UpdatePasswordResponse();

        try {
            // Find user credentials
            Credentials existingCredentials = credentialRepository.findByUserEmail(request.getUserEmail());

            if (existingCredentials == null) {
                response.setMessage("User not found with the provided email address.");
                response.setStatus("404");
                response.setResponseCode("3001");
                return response;
            }

            // Verify old password
            boolean isOldPasswordValid = bCryptPasswordEncoder.matches(
                    request.getOldPassword(),
                    existingCredentials.getUserPassword()
            );

            if (!isOldPasswordValid) {
                response.setMessage("Invalid old password provided.");
                response.setStatus("401");
                response.setResponseCode("3002");
                return response;
            }

            // Encode and update new password
            String encodedNewPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
            existingCredentials.setUserPassword(encodedNewPassword);
            credentialRepository.save(existingCredentials);

            response.setMessage("Password updated successfully.");
            response.setStatus("200");
            response.setResponseCode("1000");

        } catch (Exception e) {
            response.setMessage("Failed to update password: " + e.getMessage());
            response.setStatus("500");
            response.setResponseCode("3003");
        }

        return response;
    }
}


