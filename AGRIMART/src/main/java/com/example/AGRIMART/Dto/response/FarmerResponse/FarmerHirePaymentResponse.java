package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerHire;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerHirePaymentResponse extends Response {
    private FarmerHire farmerHirePaymentResponse;

}