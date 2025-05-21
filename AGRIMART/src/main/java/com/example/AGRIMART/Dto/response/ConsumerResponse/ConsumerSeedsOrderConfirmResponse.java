package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerSeedsOrderConfirmResponse extends Response {
    private ConsumerSeedsOrder consumerSeedsOrderConfirmResponse;

}

