package com.example.AGRIMART.Dto.response.SFResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.SFEntity.SFOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SFOrderRejectResponse extends Response {
    private SFOrder sfOrderRejectResponse;

}
