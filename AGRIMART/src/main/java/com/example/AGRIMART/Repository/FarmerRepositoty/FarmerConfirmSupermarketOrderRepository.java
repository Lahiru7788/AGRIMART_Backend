package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerConfirmSupermarketOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmerConfirmSupermarketOrderRepository extends JpaRepository<FarmerConfirmSupermarketOrder,Integer> {
    Optional<FarmerConfirmSupermarketOrder> findByProductName(String ProductName);
    FarmerConfirmSupermarketOrder findByConfirmOrderID(int confirmOrderID);
    List<FarmerConfirmSupermarketOrder> findBySupermarketAddOrder_OrderID(int orderID);
    List<FarmerConfirmSupermarketOrder> findByUser_UserID(int userID);
    //    List<ConsumerSeedsOrder> findBySFProduct_User_UserID(int userID);
    List<FarmerConfirmSupermarketOrder> findBySupermarketAddOrder_User_UserID(int userID);
//    List<ConsumerOrder> findByUser_UserID(int userID);

}
