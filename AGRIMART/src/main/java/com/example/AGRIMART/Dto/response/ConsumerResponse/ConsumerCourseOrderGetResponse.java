package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ConsumerCourseOrderGetResponse extends Response {
    private List<ConsumerCourseOrderDto> consumerCourseOrderGetResponse;
}

