package com.example.AGRIMART.Controller.SFController;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderImageDto;
import com.example.AGRIMART.Dto.SFDto.SFOrderImageDto;
import com.example.AGRIMART.Dto.response.ConsumerResponse.ConsumerOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFOrderImageAddResponse;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrderImage;
import com.example.AGRIMART.Entity.SFEntity.SFOrderImage;
import com.example.AGRIMART.Repository.ConsumerRepository.ConsumerOrderImageRepository;
import com.example.AGRIMART.Repository.SFRepository.SFOrderImageRepository;
import com.example.AGRIMART.Service.ConsumerService.ConsumerOrderImageService;
import com.example.AGRIMART.Service.SFService.SFOrderImageService;
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
public class SFOrderImageController {

    @Autowired
    private SFOrderImageService sfOrderImageService;

    @Autowired
    private SFOrderImageRepository sfOrderImageRepository;

    @PostMapping(path = "/sfOrderProductImage")
    public SFOrderImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            SFOrderImageDto sfOrderImageDto = new SFOrderImageDto();
            sfOrderImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return sfOrderImageService.save(sfOrderImageDto);

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

    @GetMapping("/viewSFOrderImage")
    public ResponseEntity<byte[]> getSFOrderImage(@RequestParam int orderID) {
        try {
            Optional<SFOrderImage> image = sfOrderImageRepository.findBySFOrder_OrderID(orderID);
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
