package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.SFDto.SFOfferDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SFOfferGetResponse extends Response {
    private List<SFOfferDto> sfOfferGetResponse;
}
