package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.UserDto;
import com.example.AGRIMART.Dto.response.UserAddResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserAddResponse save(UserDto userDto);
}
