package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.UserOrderDetailsDto;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;
import com.example.AGRIMART.Dto.response.UserOrderDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserOrderDetailsGetResponse;

public interface UserOrderDetailsService {

    UserOrderDetailsAddResponse save(UserOrderDetailsDto userOrderDetailsDto);
    UserOrderDetailsGetResponse getUserOrderDetailsByUserID(int userID);
    UserOrderDetailsGetResponse GetAllUserOrderDetails();
}
