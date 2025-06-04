package com.example.AGRIMART.Controller.TrainerController;

import com.example.AGRIMART.Dto.SupermarketDto.SAddOrderImageDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseImageDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SAddOrderImageAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseImageAddResponse;
import com.example.AGRIMART.Entity.SupermarketEntity.SAddOrderImage;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseImage;
import com.example.AGRIMART.Repository.SupermarketRepository.SAddOrderImageRepository;
import com.example.AGRIMART.Repository.TrainerRepository.TrainerCourseImageRepository;
import com.example.AGRIMART.Service.SupermarketService.SAddOrderImageService;
import com.example.AGRIMART.Service.TrainerService.TrainerCourseImageService;
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
public class TrainerCourseImageController {

    @Autowired
    private TrainerCourseImageService trainerCourseImageService;

    @Autowired
    private TrainerCourseImageRepository trainerCourseImageRepository;

    @PostMapping(path = "/trainerCourseImage")
    public TrainerCourseImageAddResponse save(
            @RequestParam("courseImage") MultipartFile courseImage) {

        try {
            // Convert MultipartFile to byte[]
            byte[] courseImageBytes = courseImage.getBytes();

            TrainerCourseImageDto trainerCourseImageDto = new TrainerCourseImageDto();
            trainerCourseImageDto.setCourseImage(courseImageBytes);

            // Delegate the save operation to the service
            return trainerCourseImageService.save(trainerCourseImageDto);

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

    @GetMapping("/viewTrainerCourseImage")
    public ResponseEntity<byte[]> getTrainerCourseImage(@RequestParam int courseID) {
        try {
            Optional<TrainerCourseImage> image = trainerCourseImageRepository.findByTrainerCourse_CourseID(courseID);
            if (image.isPresent()) {
                String contentType = detectImageType(image.get().getCourseImage());

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(image.get().getCourseImage());
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
