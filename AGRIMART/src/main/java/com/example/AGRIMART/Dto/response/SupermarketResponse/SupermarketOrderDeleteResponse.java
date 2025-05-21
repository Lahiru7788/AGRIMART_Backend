package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SupermarketOrderDeleteResponse extends Response {
    private SupermarketOrder supermarketOrderDeleteResponse;

}
