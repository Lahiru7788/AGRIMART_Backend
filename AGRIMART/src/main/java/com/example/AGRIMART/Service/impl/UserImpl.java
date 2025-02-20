package com.example.AGRIMART.Service.impl;

import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.UserAddResponse;
import com.example.AGRIMART.Entity.User;
import com.example.AGRIMART.Repository.UserRepository;
import com.example.AGRIMART.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserAddResponse save(UserDto userDto) {

        User user = modelMapper.map(userDto, User.class);
        user.setActive(true);

        //set response
        UserAddResponse response =new UserAddResponse();
        try {
            User saveNewUser = userRepository.save(user);
            if (saveNewUser != null) {

                response.setMessage("New User added Successful");
                response.setStatus("200");
                response.setResponseCode("1000");
            } else {
                response.setMessage("New User added Failure");
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
