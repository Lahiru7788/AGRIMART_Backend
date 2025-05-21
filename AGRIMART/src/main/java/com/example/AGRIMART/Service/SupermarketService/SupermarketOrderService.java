package com.example.AGRIMART.Service.SupermarketService;

import com.example.AGRIMART.Dto.SupermarketDto.SupermarketOrderDto;
import com.example.AGRIMART.Dto.response.SupermarketResponse.*;

public interface SupermarketOrderService {

    SupermarketOrderAddResponse saveOrUpdate(SupermarketOrderDto supermarketOrderDto);
    //    ConsumerAddOrderGetResponse GetAllConsumerOrders();
//    ConsumerOrderGetResponse getConsumerOrderByProductId(int productID);
    SupermarketOrderGetResponse getSupermarketOrderByUserId(int userID);
    SupermarketOrderDeleteResponse DeleteSupermarketOrderResponse(int orderID);
    SupermarketOrderGetResponse getSupermarketOrdersByProductId(int productID);
    SupermarketOrderGetResponse getSupermarketOrderByFarmerUserId(int userID);
    SupermarketOrderConfirmResponse ConfirmSupermarketOrderResponse(int orderID);
    SupermarketOrderRejectResponse RejectSupermarketOrderResponse(int orderID);
}
