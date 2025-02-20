package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Entity.UserProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileAddResponse extends Response {
    private UserProfile userProfile;

}
