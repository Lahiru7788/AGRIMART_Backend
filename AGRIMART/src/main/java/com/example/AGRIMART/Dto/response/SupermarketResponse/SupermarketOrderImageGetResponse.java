package com.example.AGRIMART.Dto.response.SupermarketResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderImageDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SupermarketOrderImageGetResponse extends Response {
    private List<SupermarketOrderImageDto> SupermarketOrderImageGetResponse;

}