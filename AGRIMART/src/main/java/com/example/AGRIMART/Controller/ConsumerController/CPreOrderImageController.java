package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.CPreOrderImageDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CPreOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CPreOrderImageGetResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;
import com.example.AGRIMART.Service.ConsumerService.CPreOrderImageService;
import com.example.AGRIMART.Service.FarmerService.FarmerProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class CPreOrderImageController {

    @Autowired
    private CPreOrderImageService cPreOrderImageService;

    @PostMapping(path = "/cPreOrderProductImage")
    public CPreOrderImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            CPreOrderImageDto cPreOrderImageDto = new CPreOrderImageDto();
            cPreOrderImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return cPreOrderImageService.save(cPreOrderImageDto);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @GetMapping("/viewPreOrderProductImage")

    public CPreOrderImageGetResponse getAllConsumerPreOrderImages() {
        return cPreOrderImageService.GetAllConsumerPreOrderImages();

    }
}
