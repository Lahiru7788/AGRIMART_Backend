package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerSeedsOrderAddToCartResponse extends Response {
    private FarmerSeedsOrder farmerSeedsOrderAddToCartResponse;
}