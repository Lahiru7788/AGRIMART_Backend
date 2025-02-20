package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageGetResponse;
import com.example.AGRIMART.Service.ConsumerService.CAddOrderImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class CAddOrderImageController {

    @Autowired
    private CAddOrderImageService cAddOrderImageService;

    @PostMapping(path = "/cAddOrderProductImage")
    public CAddOrderImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            CAddOrderImageDto cAddOrderImageDto = new CAddOrderImageDto();
            cAddOrderImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return cAddOrderImageService.save(cAddOrderImageDto);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @GetMapping("/viewConsumerAddOrderImage")

    public CAddOrderImageGetResponse getAllConsumerAddOrderImages() {
        return cAddOrderImageService.GetAllConsumerAddOrderImages();

    }
}
