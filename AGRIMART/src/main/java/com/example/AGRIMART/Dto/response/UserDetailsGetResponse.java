package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Entity.UserDetails;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDetailsGetResponse extends Response{
    private List<UserDetailsDto> userDetailsGetResponse;
}
