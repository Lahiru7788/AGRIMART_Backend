package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FarmerProductGetResponse extends Response {
    private List<FarmerProductDto> farmerProductGetResponse;
}
