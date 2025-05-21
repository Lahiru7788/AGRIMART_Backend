package com.example.AGRIMART.Controller.SupermarketController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderImageAddResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrderImage;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrderImage;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderImageRepository;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketOrderImageRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderImageService;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOrderImageService;
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
public class SupermarketSeedsOrderImageController {

    @Autowired
    private SupermarketOrderImageService supermarketOrderImageService;

    @Autowired
    private SupermarketOrderImageRepository supermarketOrderImageRepository;

    @PostMapping(path = "/supermarketSeedsOrderProductImage")
    public SupermarketOrderImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            SupermarketOrderImageDto supermarketOrderImageDto = new SupermarketOrderImageDto();
            supermarketOrderImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return supermarketOrderImageService.save(supermarketOrderImageDto);

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

    @GetMapping("/viewSupermarketSeedsOrderImage")
    public ResponseEntity<byte[]> getSupermarketOrderImage(@RequestParam int orderID) {
        try {
            Optional<SupermarketOrderImage> image = supermarketOrderImageRepository.findBySupermarketOrder_OrderID(orderID);
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
