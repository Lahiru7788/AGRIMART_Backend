package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Dto.UserProfileDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfileGetResponse extends Response{
    private List<UserProfileDto> userProfileGetResponse;
}
