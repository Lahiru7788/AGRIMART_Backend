package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupermarketAddOrderDeleteResponse extends Response {
    private SupermarketAddOrder supermarketAddOrderDeleteResponse;
}
