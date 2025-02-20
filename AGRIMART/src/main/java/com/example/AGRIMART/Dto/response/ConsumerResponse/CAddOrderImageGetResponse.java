package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;

import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CAddOrderImageGetResponse extends Response {
    private List<CAddOrderImageDto> CAddOrderImageGetResponse;

}
