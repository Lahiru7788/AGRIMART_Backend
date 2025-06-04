package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderImageDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerOrderImageDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerOrderImageAddResponse;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrderImage;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrderImage;
import com.example.AGRIMART.Repository.SupermarketRepository.SupermarketOrderImageRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerOrderImageRepository;
import com.example.AGRIMART.Service.SupermarketService.SupermarketOrderImageService;
import com.example.AGRIMART.Service.TrainerService.TrainerOrderImageService;
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
public class TrainerOrderImageController {

    @Autowired
    private TrainerOrderImageService trainerOrderImageService;

    @Autowired
    private TrainerOrderImageRepository trainerOrderImageRepository;

    @PostMapping(path = "/trainerOrderProductImage")
    public TrainerOrderImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            TrainerOrderImageDto trainerOrderImageDto = new TrainerOrderImageDto();
            trainerOrderImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return trainerOrderImageService.save(trainerOrderImageDto);

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

    @GetMapping("/viewTrainerOrderImage")
    public ResponseEntity<byte[]> getTrainerOrderImage(@RequestParam int orderID) {
        try {
            Optional<TrainerOrderImage> image = trainerOrderImageRepository.findByTrainerOrder_OrderID(orderID);
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
