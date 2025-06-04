package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerSeedsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmerSeedsOrderRepository extends JpaRepository<FarmerSeedsOrder,Integer> {
    Optional<FarmerSeedsOrder> findByProductName(String ProductName);
    FarmerSeedsOrder findByOrderID(int orderID);
    List<FarmerSeedsOrder> findBySFProduct_ProductID(int productID);
    List<FarmerSeedsOrder> findByUser_UserID(int userID);
    //    List<ConsumerSeedsOrder> findBySFProduct_User_UserID(int userID);
    List<FarmerSeedsOrder> findBySFProduct_User_UserID(int userID);
//    List<ConsumerOrder> findByUser_UserID(int userID);

}
