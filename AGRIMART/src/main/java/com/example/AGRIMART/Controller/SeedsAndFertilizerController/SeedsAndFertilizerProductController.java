package com.example.AGRIMART.Controller.SeedsAndFertilizerController;

import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SeedsAndFetilizerProductDto;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SeedsAndFertilizerProductAddResponse;
import com.example.AGRIMART.Dto.response.SeedsAndFertilizerResponse.SeedsAndFertilizerProductGetResponse;
import com.example.AGRIMART.Service.SeedsAndFertilizerService.SeedsAndFertilizerProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("api/user")

public class SeedsAndFertilizerProductController {

    @Autowired
    private SeedsAndFertilizerProductService seedsAndFertilizerProductService;

    @PostMapping(path = "/seedsAndFertilizerProduct")
    public SeedsAndFertilizerProductAddResponse save(@RequestBody SeedsAndFetilizerProductDto seedsAndFetilizerProductDto, HttpSession session){
        SeedsAndFertilizerProductAddResponse response = seedsAndFertilizerProductService.saveOrUpdate(seedsAndFetilizerProductDto);

        if ("200".equals(response.getStatus())) {
            session.setAttribute("productID", seedsAndFetilizerProductDto.getProductID());
        }

        return response;
    }

    @GetMapping("/viewSeedsAndFertilizerProduct")

    public SeedsAndFertilizerProductGetResponse getAllSeedsAndFertilizerProducts() {
        return seedsAndFertilizerProductService.GetAllSeedsAndFertilizerProducts();

    }
}
