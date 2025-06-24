package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Dto.UserCategoriesDto;
import com.example.AGRIMART.Dto.UserOrderDetailsDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserOrderDetailsGetResponse extends Response {
    private List<UserOrderDetailsDto> userOrderDetailsGetResponse;
}

