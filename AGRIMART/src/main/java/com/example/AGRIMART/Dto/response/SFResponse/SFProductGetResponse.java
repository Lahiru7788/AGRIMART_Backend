package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SFProductGetResponse extends Response {
    private List<SFProductDto> seedsAndFertilizerProductGetResponse;
}
