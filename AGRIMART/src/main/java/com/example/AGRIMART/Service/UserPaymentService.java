package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.UserPaymentDto;
import com.example.AGRIMART.Dto.response.UserPaymentAddResponse;

public interface UserPaymentService {

    UserPaymentAddResponse save(UserPaymentDto userPaymentDto);

}
