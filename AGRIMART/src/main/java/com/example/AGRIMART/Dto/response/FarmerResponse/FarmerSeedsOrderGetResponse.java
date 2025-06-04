package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FarmerSeedsOrderGetResponse extends Response {
    private List<FarmerSeedsOrderDto> farmerSeedsOrderGetResponse;
}
