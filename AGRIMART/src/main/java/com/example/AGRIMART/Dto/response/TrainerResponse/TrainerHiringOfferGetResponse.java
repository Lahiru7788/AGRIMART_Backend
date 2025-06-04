package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.TrainerDto.TrainerHiringOfferDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrainerHiringOfferGetResponse extends Response {
    private List<TrainerHiringOfferDto> trainerHiringOfferGetResponse;

}