package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.SFEntity.SFOffer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SFOfferDeleteResponse extends Response {
    private SFOffer sfOfferDeleteResponse;
}
