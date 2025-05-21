package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketSeedsOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SupermarketSeedsOrderGetResponse extends Response {
    private List<SupermarketSeedsOrderDto> supermarketSeedsOrderGetResponse;
}
