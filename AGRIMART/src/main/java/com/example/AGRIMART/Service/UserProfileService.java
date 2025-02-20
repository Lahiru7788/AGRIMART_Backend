package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.UserProfileDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;
import com.example.AGRIMART.Dto.response.UserProfileAddResponse;
import com.example.AGRIMART.Dto.response.UserProfileGetResponse;

public interface UserProfileService {
    UserProfileAddResponse save(UserProfileDto userProfileDto);
    UserProfileGetResponse GetAllUserProfiles();
}
