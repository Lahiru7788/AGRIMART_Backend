package com.example.AGRIMART.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialDto {
   private int credentialId;
    private String userEmail;
    private String userPassword;
    private UserDto user;
//
//    private String lastName;
//

}
