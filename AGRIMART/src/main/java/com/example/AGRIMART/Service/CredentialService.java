package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.CredentialDto;
import com.example.AGRIMART.Dto.response.CredentialAddResponse;


public interface CredentialService {
//    String saveCredentials(CredentialDto credentialDto);

    CredentialAddResponse save(CredentialDto credentialDto);
}

