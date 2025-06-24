package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmConsumerOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmerConfirmConsumerOrderRepository extends JpaRepository<FarmerConfirmConsumerOrder,Integer> {
    Optional<FarmerConfirmConsumerOrder> findByProductName(String ProductName);
    FarmerConfirmConsumerOrder findByConfirmOrderID(int confirmOrderID);
    List<FarmerConfirmConsumerOrder> findByConsumerAddOrder_OrderID(int orderID);
    List<FarmerConfirmConsumerOrder> findByUser_UserID(int userID);
    //    List<ConsumerSeedsOrder> findBySFProduct_User_UserID(int userID);
    List<FarmerConfirmConsumerOrder> findByConsumerAddOrder_User_UserID(int userID);
//    List<ConsumerOrder> findByUser_UserID(int userID);

}

