package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainerOrderConfirmResponse extends Response {
    private TrainerOrder trainerOrderConfirmResponse;

}
