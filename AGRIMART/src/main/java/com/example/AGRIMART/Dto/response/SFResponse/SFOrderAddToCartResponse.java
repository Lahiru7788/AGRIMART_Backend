package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.SFEntity.SFOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SFOrderAddToCartResponse extends Response {
    private SFOrder sfOrderAddToCartResponse;
}