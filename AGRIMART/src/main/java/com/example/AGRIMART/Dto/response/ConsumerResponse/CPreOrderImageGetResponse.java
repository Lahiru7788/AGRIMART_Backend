package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.ConsumerDto.CPreOrderImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CPreOrderImageGetResponse extends Response {
    private List<CPreOrderImageDto> cPreOrderGetResponse;

}
