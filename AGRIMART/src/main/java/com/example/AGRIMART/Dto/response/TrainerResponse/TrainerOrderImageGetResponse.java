package com.example.AGRIMART.Dto.response.TrainerResponse;

import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrainerOrderImageGetResponse extends Response {
    private List<TrainerOrderImageDto> trainerOrderImageGetResponse;

}