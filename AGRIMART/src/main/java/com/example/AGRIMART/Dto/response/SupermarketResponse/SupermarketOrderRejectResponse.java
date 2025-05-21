package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupermarketOrderRejectResponse extends Response {
    private SupermarketOrder supermarketOrderRejectResponse;

}
