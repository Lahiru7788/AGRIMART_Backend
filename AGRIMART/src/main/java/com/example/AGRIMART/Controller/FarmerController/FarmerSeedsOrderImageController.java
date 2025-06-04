package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerSeedsOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerSeedsOrderImageAddResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrderImage;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrderImage;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerSeedsOrderImageRepository;
import com.example.AGRIMART.Repository.FarmerRepositoty.FarmerSeedsOrderImageRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerSeedsOrderImageService;
import com.example.AGRIMART.Service.FarmerService.FarmerSeedsOrderImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")
public class FarmerSeedsOrderImageController {

    @Autowired
    private FarmerSeedsOrderImageService farmerSeedsOrderImageService;

    @Autowired
    private FarmerSeedsOrderImageRepository farmerSeedsOrderImageRepository;

    @PostMapping(path = "/farmerSeedsOrderProductImage")
    public FarmerSeedsOrderImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            FarmerSeedsOrderImageDto farmerSeedsOrderImageDto = new FarmerSeedsOrderImageDto();
            farmerSeedsOrderImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return farmerSeedsOrderImageService.save(farmerSeedsOrderImageDto);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

//    @GetMapping("/viewConsumerAddOrderImage")
//
//    public CAddOrderImageGetResponse getAllConsumerAddOrderImages() {
//        return cAddOrderImageService.GetAllConsumerAddOrderImages();
//
//    }

    @GetMapping("/viewFarmerSeedsOrderImage")
    public ResponseEntity<byte[]> getFarmerSeedsOrderImage(@RequestParam int orderID) {
        try {
            Optional<FarmerSeedsOrderImage> image = farmerSeedsOrderImageRepository.findByFarmerSeedsOrder_OrderID(orderID);
            if (image.isPresent()) {
                String contentType = detectImageType(image.get().getProductImage());

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(image.get().getProductImage());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    private String detectImageType(byte[] imageBytes) {
        if (imageBytes.length >= 8) {
            if ((imageBytes[0] & 0xFF) == 0x89 && (imageBytes[1] & 0xFF) == 0x50) {
                return "image/png";
            } else if ((imageBytes[0] & 0xFF) == 0xFF && (imageBytes[1] & 0xFF) == 0xD8) {
                return "image/jpeg";
            }
        }
        return "application/octet-stream";
    }
}
