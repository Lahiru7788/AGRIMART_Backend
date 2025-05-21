package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketSeedsOrderImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SupermarketSeedsOrderImageGetResponse extends Response {
    private List<SupermarketSeedsOrderImageDto> supermarketSeedsOrderImageGetResponse;

}
