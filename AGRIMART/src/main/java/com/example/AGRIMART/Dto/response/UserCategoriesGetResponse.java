package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Dto.UserCategoriesDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserCategoriesGetResponse extends Response {
    private List<UserCategoriesDto> userCategoriesGetResponse;
}
