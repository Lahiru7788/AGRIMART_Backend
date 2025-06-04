package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;
import com.example.AGRIMART.Service.FarmerService.FarmerProductService;
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

public class FarmerProductController {

    @Autowired
    private FarmerProductService farmerProductService;

//    @PostMapping(path = "/farmerProducts")
//    public FarmerProductAddResponse save(@RequestBody FarmerProductDto farmerProductDto, HttpSession session) {
//        FarmerProductAddResponse response = farmerProductService.saveOrUpdate(farmerProductDto);
//        System.out.println("Response Status: " + response.getStatus());
//        System.out.println("Product ID: " + farmerProductDto.getProductID());
//        if ("200".equals(response.getStatus())) {
//            session.setAttribute("productID", farmerProductDto.getProductID());
//            System.out.println("Product ID set in session: " + farmerProductDto.getProductID());
//        } else {
//            System.out.println("Failed to set product ID in session due to status: " + response.getStatus());
//        }
//        return response;
//    }

    @PostMapping(path = "/farmerProducts")
    public FarmerProductAddResponse save(@Valid @RequestBody FarmerProductDto farmerProductDto, HttpSession session) {
        System.out.println("Received FarmerProductDto: " + farmerProductDto);
        FarmerProductAddResponse response = farmerProductService.saveOrUpdate(farmerProductDto);
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Product ID from DTO: " + farmerProductDto.getProductID());
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

    @GetMapping("/viewFarmerProducts")

    public FarmerProductGetResponse getAllFarmerProducts() {
        return farmerProductService.GetAllFarmerProducts();

    }

    @GetMapping("/viewFarmerProducts/{userID}")
    public FarmerProductGetResponse findByUser_UserID(@PathVariable("userID") int userID) {
        return farmerProductService.getFarmerProductByUserId(userID);
    }

    @PutMapping(value = "/farmer-product/{productID}/delete")
    public FarmerProductDeleteResponse DeleteFarmerResponse(@PathVariable int productID){
        return farmerProductService.DeleteFarmerResponse(productID);

    }

}
