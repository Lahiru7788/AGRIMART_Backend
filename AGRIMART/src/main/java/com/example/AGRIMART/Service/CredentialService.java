package com.example.AGRIMART.Service;

import com.example.AGRIMART.Dto.CredentialDto;
import com.example.AGRIMART.Dto.response.CredentialAddResponse;
import com.example.AGRIMART.Dto.request.ForgotPasswordRequest;
import com.example.AGRIMART.Dto.request.UpdatePasswordRequest;
import com.example.AGRIMART.Dto.response.ForgotPasswordResponse;
import com.example.AGRIMART.Dto.response.UpdatePasswordResponse;

public interface CredentialService {
    CredentialAddResponse save(CredentialDto credentialDto);

    // Forgot password functionality
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);

    // Update password functionality
    UpdatePasswordResponse updatePassword(UpdatePasswordRequest request);
}