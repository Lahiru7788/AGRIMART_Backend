package com.example.AGRIMART.Dto.response;

import lombok.*;

@Getter
@Setter
@ToString
/////
@Data
////////
public class Response {
    private String ResponseCode;
    private String status;
    private String message;
    private Integer productID;
    private Integer orderID;
    private Integer courseID;
    private Integer hireID;
}
