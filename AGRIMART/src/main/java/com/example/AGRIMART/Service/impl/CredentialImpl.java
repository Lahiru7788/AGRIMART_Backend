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
//    }
//}

package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.CredentialDto;

import com.example.AGRIMART.Dto.response.CredentialAddResponse;
import com.example.AGRIMART.Dto.response.UserAddResponse;
import com.example.AGRIMART.Entity.Credentials;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.CredentialRepository;

import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.CredentialService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialImpl implements CredentialService {

    @Autowired
    private CredentialRepository credentialRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Override
    public CredentialAddResponse save(CredentialDto credentialDto) {
        Optional<User> userOptional = userRepository.findByUserEmail(credentialDto.getUserEmail());

//        if (userOptional.isEmpty()) {
//            return "User not found for the given email.";
//        }
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

//        credentialRepository.save(credentials);
//        return "Credentials saved successfully.";
        CredentialAddResponse response =new CredentialAddResponse();
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
            // Handle the exception
            response.setMessage("Error: " + e.getMessage()); // Customize the error message as needed
            response.setStatus("500"); // Internal server error status
        }


        return response;
    }
}

