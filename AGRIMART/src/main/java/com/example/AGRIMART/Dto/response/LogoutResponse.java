package com.example.AGRIMART.Dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutResponse {
    private String status;
    private String message;
    private String code;
}
