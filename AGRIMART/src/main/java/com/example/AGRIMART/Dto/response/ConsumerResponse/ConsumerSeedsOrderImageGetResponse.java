package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConsumerSeedsOrderImageGetResponse extends Response {
    private List<ConsumerSeedsOrderImageDto> consumerSeedsOrderImageGetResponse;

}
