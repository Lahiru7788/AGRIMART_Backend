package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketSeedsOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupermarketSeedsOrderConfirmResponse extends Response {
    private SupermarketSeedsOrder supermarketSeedsOrderConfirmResponse;

}