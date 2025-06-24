package com.example.AGRIMART.Service;


import com.example.AGRIMART.Dto.UserDetailsDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;
import com.example.AGRIMART.Dto.response.UserDetailsAddResponse;
import com.example.AGRIMART.Dto.response.UserDetailsGetResponse;


public interface UserDetailsService {

    UserDetailsAddResponse save(UserDetailsDto userDetailsDto);
    UserDetailsGetResponse getUserDetailsByUserID(int userID);
    UserDetailsGetResponse GetAllUserDetails();
}
