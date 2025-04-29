package com.example.AGRIMART.Controller.FarmerController;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductAddResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductDeleteResponse;
import com.example.AGRIMART.Dto.response.FarmerResponse.FarmerProductGetResponse;
import com.example.AGRIMART.Service.FarmerService.FarmerProductService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping(path = "/farmerProducts")
    public FarmerProductAddResponse save(@RequestBody FarmerProductDto farmerProductDto, HttpSession session){
        FarmerProductAddResponse response = farmerProductService.saveOrUpdate(farmerProductDto);

        if ("200".equals(response.getStatus())) {
            session.setAttribute("productID", farmerProductDto.getProductName());
        }

        return response;
    }

    @GetMapping("/viewFarmerProducts")

    public FarmerProductGetResponse getAllFarmerProducts() {
        return farmerProductService.GetAllFarmerProducts();

    }

    @PutMapping(value = "/farmer-product/{productID}/delete")
    public FarmerProductDeleteResponse DeleteFarmerResponse(@PathVariable int productID){
        return farmerProductService.DeleteFarmerResponse(productID);

    }

}
