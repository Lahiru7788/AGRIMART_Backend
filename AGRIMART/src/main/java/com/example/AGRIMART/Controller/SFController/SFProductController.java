package com.example.AGRIMART.Controller.SFController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SeedsAndFertilizerDeleteResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductAddResponse;
import com.example.AGRIMART.Dto.response.SFResponse.SFProductGetResponse;
import com.example.AGRIMART.Service.SFService.SFProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
//
//    @PostMapping(path = "/seedsAndFertilizerProduct")
//    public SFProductAddResponse save(@RequestBody SFProductDto SFProductDto, HttpSession session){
//        SFProductAddResponse response = SFProductService.saveOrUpdate(SFProductDto);
//
//        if ("200".equals(response.getStatus())) {
//            session.setAttribute("productID", SFProductDto.getProductName());
//        }
//
//        return response;
//    }

    @PostMapping(path = "/seedsAndFertilizerProduct")
    public SFProductAddResponse save(@Valid @RequestBody SFProductDto SFProductDto, HttpSession session) {
        System.out.println("Received FarmerProductDto: " + SFProductDto);
        SFProductAddResponse response = SFProductService.saveOrUpdate(SFProductDto);
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Product ID from DTO: " + SFProductDto.getProductID());
        System.out.println("Product ID from Response: " + response.getProductID());
        if ("200".equals(response.getStatus()) && response.getProductID() != null) {
            int productID = response.getProductID(); // Use auto-generated ID from response
            session.setAttribute("productID", productID);
            System.out.println("Product ID set in session: " + productID);
        } else {
            System.out.println("Failed to set product ID in session due to status: " + response.getStatus());
        }
        System.out.println("Session productID: " + session.getAttribute("productID"));
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
