package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmSupermarketOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerCourseOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FarmerConfirmSupermarketOrderGetResponse extends Response {
    private List<FarmerConfirmSupermarketOrderDto> farmerConfirmSupermarketOrderGetResponse;
}
