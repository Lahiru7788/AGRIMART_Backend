package com.example.AGRIMART.Dto.response;

import com.example.AGRIMART.Entity.Credentials;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialAddResponse extends Response{

    private Credentials credentials;
}
