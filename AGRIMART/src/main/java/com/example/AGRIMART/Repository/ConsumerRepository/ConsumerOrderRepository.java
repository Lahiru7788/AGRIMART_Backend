package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumerOrderRepository extends JpaRepository<ConsumerOrder,Integer> {
    Optional<ConsumerOrder> findByProductName(String ProductName);
    ConsumerOrder findByOrderID(int orderID);
    List<ConsumerOrder> findByFarmerProduct_ProductID(int productID);
    List<ConsumerOrder> findByUser_UserID(int userID);
    List<ConsumerOrder> findByFarmerProduct_User_UserID(int userID);
//    List<ConsumerOrder> findByUser_UserID(int userID);

}
