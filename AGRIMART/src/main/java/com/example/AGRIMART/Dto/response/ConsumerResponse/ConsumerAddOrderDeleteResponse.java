package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerAddOrderDeleteResponse extends Response {
    private ConsumerAddOrder consumerAddOrderDeleteResponse;

}
