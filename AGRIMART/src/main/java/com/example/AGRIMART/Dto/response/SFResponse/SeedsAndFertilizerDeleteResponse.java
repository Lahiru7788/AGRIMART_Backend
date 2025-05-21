package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeedsAndFertilizerDeleteResponse  extends Response {
    private SFProduct SFProductDeleteResponse;
}
