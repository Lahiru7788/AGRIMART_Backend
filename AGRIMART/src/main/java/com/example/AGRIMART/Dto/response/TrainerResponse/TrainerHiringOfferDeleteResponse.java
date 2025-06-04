package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiringOffer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrainerHiringOfferDeleteResponse extends Response {
    private TrainerHiringOffer trainerHiringOfferDeleteResponse;
}
