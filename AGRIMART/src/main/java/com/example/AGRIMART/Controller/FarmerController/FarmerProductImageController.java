package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.UserProfileDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerOfferGetResponse;
import com.example.AGRIMART.Dto.response.UserProfileAddResponse;
import com.example.AGRIMART.Service.FarmerService.FarmerProductImageService;
import com.example.AGRIMART.Service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class FarmerProductImageController {

    @Autowired
    private FarmerProductImageService farmerProductImageService;

    @PostMapping(path = "/farmerProductImage")
    public FProductImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            FarmerProductImageDto farmerProductImageDto = new FarmerProductImageDto();
            farmerProductImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return farmerProductImageService.save(farmerProductImageDto);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @GetMapping("/viewFarmerProductImage")

    public FProductImageGetResponse getAllFarmerProductImages() {
        return farmerProductImageService.GetAllFarmerProductImages();

    }
}
