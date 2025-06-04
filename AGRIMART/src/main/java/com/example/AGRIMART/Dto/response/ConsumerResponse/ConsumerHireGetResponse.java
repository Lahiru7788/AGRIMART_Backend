package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerHireDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOfferDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerHire;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ConsumerHireGetResponse extends Response {
    private List<ConsumerHireDto> consumerHireGetResponse;
}
