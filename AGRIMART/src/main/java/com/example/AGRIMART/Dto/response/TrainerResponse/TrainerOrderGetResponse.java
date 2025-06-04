package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TrainerOrderGetResponse extends Response {
    private List<TrainerOrderDto> trainerOrderGetResponse;
}
