package com.example.AGRIMART.Dto.response.ConsumerResponse;

import com.example.AGRIMART.Dto.response.Response;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerHire;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerHireConfirmResponse extends Response {
    private ConsumerHire consumerHireConfirmResponse;

}
