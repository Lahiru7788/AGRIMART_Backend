package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.SFDto.SFProductImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SFProductImageGetResponse extends Response {
    private List<SFProductImageDto> sandFProductImageGetResponse;
}
