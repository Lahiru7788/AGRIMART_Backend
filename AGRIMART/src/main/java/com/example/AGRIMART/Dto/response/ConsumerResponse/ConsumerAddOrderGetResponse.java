package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ConsumerAddOrderGetResponse extends Response {
    private List<ConsumerAddOrderDto> consumerAddOrderGetResponse;
}
