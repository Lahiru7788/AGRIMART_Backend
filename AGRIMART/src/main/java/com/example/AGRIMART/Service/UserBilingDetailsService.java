package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.UserBilingDetailsDto;
import com.example.AGRIMART.Dto.response.UserBilingDetailsAddResponse;

public interface UserBilingDetailsService {

    UserBilingDetailsAddResponse save(UserBilingDetailsDto userBilingDetailsDto);

}
