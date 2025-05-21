package com.example.AGRIMART.Service.SupermarketService;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketAddOrderDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderAddResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderDeleteResponse;
import com.example.AGRIMART.Dto.response.SupermarketResponse.SupermarketAddOrderGetResponse;

public interface SupermarketAddOrderService {
    SupermarketAddOrderAddResponse saveOrUpdate(SupermarketAddOrderDto supermarketAddOrderDto);
    SupermarketAddOrderGetResponse GetAllSupermarketOrders();
    SupermarketAddOrderGetResponse getSupermarketAddOrderByUserId(int userID);
    SupermarketAddOrderDeleteResponse DeleteSupermarketResponse(int orderID);
}
