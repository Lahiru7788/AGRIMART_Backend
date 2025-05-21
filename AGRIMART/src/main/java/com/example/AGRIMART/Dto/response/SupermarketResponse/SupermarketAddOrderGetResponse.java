package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketAddOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SupermarketAddOrderGetResponse extends Response {
    private List<SupermarketAddOrderDto> supermarketAddOrderGetResponse;
}
