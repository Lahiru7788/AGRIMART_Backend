package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.FarmerDto.FarmerConfirmConsumerOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerCourseOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FarmerConfirmConsumerOrderGetResponse extends Response {
    private List<FarmerConfirmConsumerOrderDto> farmerConfirmConsumerOrderGetResponse;
}