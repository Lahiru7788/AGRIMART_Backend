package com.example.AGRIMART.Controller.SeedsAndFertilizerController;

import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SandFProductImageDto;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SandFProductImageAddResponse;
import com.example.AGRIMART.Service.SeedsAndFertilizerService.SandFProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class SandFProductImageController {

    @Autowired
    private SandFProductImageService sandFProductImageService;

    @PostMapping(path = "/seedsAndFertilizerProductImage")
    public SandFProductImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage){

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            SandFProductImageDto sandFProductImageDto = new SandFProductImageDto();
            sandFProductImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return sandFProductImageService.save(sandFProductImageDto);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
