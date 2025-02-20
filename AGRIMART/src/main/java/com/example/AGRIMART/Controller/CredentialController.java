

package com.example.AGRIMART.Controller;

import com.example.AGRIMART.Dto.CredentialDto;
import com.example.AGRIMART.Dto.response.CredentialAddResponse;
import com.example.AGRIMART.Service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class CredentialController {

    @Autowired
    private CredentialService credentialService;

    @PostMapping(path = "/credentials")
    public CredentialAddResponse save(@RequestBody CredentialDto credentialDto){
        return credentialService.save(credentialDto);
    }
}

