package com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse;

import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SeedsAndFetilizerProductDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SeedsAndFertilizerProductGetResponse extends Response {
    private List<SeedsAndFetilizerProductDto> seedsAndFertilizerProductGetResponse;
}
