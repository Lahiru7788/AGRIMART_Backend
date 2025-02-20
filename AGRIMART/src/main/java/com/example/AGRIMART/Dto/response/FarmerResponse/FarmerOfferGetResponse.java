package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerOfferDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FarmerOfferGetResponse extends Response {
    private List<FarmerOfferDto> farmerOfferGetResponse;

}
