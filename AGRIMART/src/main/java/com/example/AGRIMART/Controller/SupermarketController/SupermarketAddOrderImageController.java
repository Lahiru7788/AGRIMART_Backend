package com.example.AGRIMART.Controller.SupermarketController;

import com.example.AGRIMART.Dto.ConsumerDto.CAddOrderImageDto;
import com.example.AGRIMART.Dto.SupermarketDto.SAddOrderImageDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageAddResponse;
import com.example.AGRIMART.Entity.SupermarketEntity.SAddOrderImage;
import com.example.AGRIMART.Repository.SupermarketRepository.SAddOrderImageRepository;
import com.example.AGRIMART.Service.SupermarketService.SAddOrderImageService;
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
public class SupermarketAddOrderImageController {

    @Autowired
    private SAddOrderImageService sAddOrderImageService;

    @Autowired
    private SAddOrderImageRepository sAddOrderImageRepository;

    @PostMapping(path = "/sAddOrderProductImage")
    public SAddOrderImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            SAddOrderImageDto sAddOrderImageDto = new SAddOrderImageDto();
            sAddOrderImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return sAddOrderImageService.save(sAddOrderImageDto);

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

    @GetMapping("/viewSupermarketAddOrderImage")
    public ResponseEntity<byte[]> getSAddOrderImage(@RequestParam int orderID) {
        try {
            Optional<SAddOrderImage> image = sAddOrderImageRepository.findBySupermarketAddOrder_OrderID(orderID);
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
