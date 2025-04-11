package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductImageDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FProductImageGetResponse;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProductImage;
import com.example.AGRIMART.Repository.FarmerRepositoty.FProductImageRepository;
import com.example.AGRIMART.Service.FarmerService.FarmerProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/user")
public class FarmerProductImageController {

    @Autowired
    private FarmerProductImageService farmerProductImageService;

    @Autowired
    private FProductImageRepository fProductImageRepository;
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

//    @GetMapping("/viewFarmerProductImage")
//
//    public FProductImageGetResponse getAllFarmerProductImages() {
//        return farmerProductImageService.GetAllFarmerProductImages();
//
//    }

    @GetMapping("/viewFarmerProductImage")
    public ResponseEntity<byte[]> getFarmerProductImage(@RequestParam int productID) {
        try {
            Optional<FarmerProductImage> image = fProductImageRepository.findByFarmerProduct_ProductID(productID);
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
