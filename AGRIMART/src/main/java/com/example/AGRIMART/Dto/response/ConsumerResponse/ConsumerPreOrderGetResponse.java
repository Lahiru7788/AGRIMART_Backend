package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerPreOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ConsumerPreOrderGetResponse extends Response {
    private List<ConsumerPreOrderDto> consumerPreOrderGetResponse;
}
