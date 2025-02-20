package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Dto.CredentialDto;
import com.example.AGRIMART.Entity.Credentials;
import com.example.AGRIMART.Entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {

    private String status;
    private String message;
    private String code;
    private Credentials credentials;
    private List<CredentialDto> credentialDtoList;


    public void setCredentials(List<CredentialDto> credentialDtoList) {
        this.credentialDtoList = credentialDtoList;
    }
}
