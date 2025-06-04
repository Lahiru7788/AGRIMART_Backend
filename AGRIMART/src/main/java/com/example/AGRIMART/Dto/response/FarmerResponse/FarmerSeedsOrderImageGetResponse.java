package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FarmerSeedsOrderImageGetResponse extends Response {
    private List<FarmerSeedsOrderImageDto> farmerSeedsOrderImageGetResponse;

}
