package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SFOrderGetResponse extends Response {
    private List<SFOrderDto> sfOrderGetResponse;
}