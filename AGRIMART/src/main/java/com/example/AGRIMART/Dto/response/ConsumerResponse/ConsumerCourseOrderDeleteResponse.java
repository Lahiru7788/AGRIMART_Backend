package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerCourseOrder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ConsumerCourseOrderDeleteResponse extends Response {
    private ConsumerCourseOrder consumerCourseOrderDeleteResponse;

}
