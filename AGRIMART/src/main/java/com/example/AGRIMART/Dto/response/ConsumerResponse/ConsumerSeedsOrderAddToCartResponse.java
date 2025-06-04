package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerSeedsOrderAddToCartResponse extends Response {
    private ConsumerSeedsOrder consumerSeedsOrderAddToCartResponse;
}
