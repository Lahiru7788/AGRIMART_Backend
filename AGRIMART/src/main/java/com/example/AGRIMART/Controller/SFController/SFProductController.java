package com.example.AGRIMART.Controller.SFController;

import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.response.SFResponse.SeedsAndFertilizerDeleteResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductGetResponse;
import com.example.AGRIMART.Service.SFService.SFProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(
        origins = "http://localhost:3000", // ✅ Set your frontend origin explicitly
        allowCredentials = "true"          // ✅ Allow sending session/cookies
)
@RequestMapping("api/user")

public class SFProductController {

    @Autowired
    private SFProductService SFProductService;

    @PostMapping(path = "/seedsAndFertilizerProduct")
    public SFProductAddResponse save(@RequestBody SFProductDto SFProductDto, HttpSession session){
        SFProductAddResponse response = SFProductService.saveOrUpdate(SFProductDto);

        if ("200".equals(response.getStatus())) {
            session.setAttribute("productID", SFProductDto.getProductName());
        }

        return response;
    }

    @GetMapping("/viewSeedsAndFertilizerProduct")

    public SFProductGetResponse getAllSeedsAndFertilizerProducts() {
        return SFProductService.GetAllSeedsAndFertilizerProducts();

    }

    @GetMapping("/viewSeedsAndFertilizerProduct/{userID}")
    public SFProductGetResponse findByUser_UserID(@PathVariable("userID") int userID) {
        return SFProductService.getSeedsAndFertilizerProductByUserId(userID);
    }

    @PutMapping(value = "/sAndF-product/{productID}/delete")
    public SeedsAndFertilizerDeleteResponse DeleteSeedsAndFertilizerResponse(@PathVariable int productID){
        return SFProductService.DeleteSeedsAndFertilizerResponse(productID);

    }

}
