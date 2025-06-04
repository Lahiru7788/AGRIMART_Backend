package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SFOrderImageGetResponse extends Response {
    private List<SFOrderImageDto> sfOrderImageGetResponse;

}
