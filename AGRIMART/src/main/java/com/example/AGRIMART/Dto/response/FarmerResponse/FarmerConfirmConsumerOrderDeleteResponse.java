package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmSupermarketOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerCourseOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerConfirmConsumerOrderDeleteResponse extends Response {
    private FarmerConfirmConsumerOrder farmerConfirmConsumerOrderDeleteResponse;

}