package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOfferDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SupermarketOfferGetResponse extends Response {
    private List<SupermarketOfferDto> supermarketOfferGetResponse;
}
