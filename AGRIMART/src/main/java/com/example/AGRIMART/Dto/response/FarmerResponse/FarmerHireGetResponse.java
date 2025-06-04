package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerHireDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FarmerHireGetResponse extends Response {
    private List<FarmerHireDto> farmerHireGetResponse;
}

