package com.example.AGRIMART.Controller.SFController;

import com.example.AGRIMART.Dto.SFDto.SFProductImageDto;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductImageAddResponse;
import com.example.AGRIMART.Entity.SFEntity.SFProductImage;
import com.example.AGRIMART.Repository.SFRepository.SFProductImageRepository;
import com.example.AGRIMART.Service.SFService.SFProductImageService;
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
public class SFProductImageController {

    @Autowired
    private SFProductImageService SFProductImageService;

    @Autowired
    private SFProductImageRepository SFProductImageRepository;

    @PostMapping(path = "/seedsAndFertilizerProductImage")
    public SFProductImageAddResponse save(
            @RequestParam("productImage") MultipartFile productImage){

        try {
            // Convert MultipartFile to byte[]
            byte[] productImageBytes = productImage.getBytes();

            SFProductImageDto SFProductImageDto = new SFProductImageDto();
            SFProductImageDto.setProductImage(productImageBytes);

            // Delegate the save operation to the service
            return SFProductImageService.save(SFProductImageDto);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }



    @GetMapping("/viewSAndFProductImage")
    public ResponseEntity<byte[]> getSAndFProductImage(@RequestParam int productID) {
        try {
            Optional<SFProductImage> image = SFProductImageRepository.findBySFProduct_ProductID(productID);
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
