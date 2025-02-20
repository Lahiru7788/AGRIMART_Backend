package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Entity.UserDetails;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDetailsAddResponse extends Response{
    private UserDetails userDetails;
}
