package com.example.AGRIMART.Dto.response.FarmerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FarmerOfferDeleteResponse extends Response {
    private FarmerOffer farmerOfferDeleteResponse;
}
