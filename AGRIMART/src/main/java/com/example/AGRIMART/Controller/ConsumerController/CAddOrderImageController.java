package com.example.AGRIMART.Controller.ConsumerController;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.ConsumerResponse.CAddOrderImageGetResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.CAddOrderImage;
import com.example.AGRIMART.Entity.UserProfile;
import com.example.AGRIMART.Repository.ConsumerRepository.CAddOrderImageRepository;
import com.example.AGRIMART.Service.ConsumerService.CAddOrderImageService;
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
public class CAddOrderImageController {

    @Autowired
    private CAddOrderImageService cAddOrderImageService;

    @Autowired
    private CAddOrderImageRepository cAddOrderImageRepository;

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

//    @GetMapping("/viewConsumerAddOrderImage")
//
//    public CAddOrderImageGetResponse getAllConsumerAddOrderImages() {
//        return cAddOrderImageService.GetAllConsumerAddOrderImages();
//
//    }

    @GetMapping("/viewConsumerAddOrderImage")
    public ResponseEntity<byte[]> getCAddOrderImage(@RequestParam int orderID) {
        try {
            Optional<CAddOrderImage> image = cAddOrderImageRepository.findByConsumerAddOrder_OrderID(orderID);
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
