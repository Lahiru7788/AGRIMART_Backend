package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmConsumerOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerConfirmConsumerOrderPaymentResponse extends Response {
    private FarmerConfirmConsumerOrder farmerConfirmConsumerOrderPaymentResponse;

}