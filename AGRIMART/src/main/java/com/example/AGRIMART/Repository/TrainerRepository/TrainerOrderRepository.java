package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerOrderRepository extends JpaRepository<TrainerOrder,Integer> {

    Optional<TrainerOrder> findByProductName(String ProductName);
    TrainerOrder findByOrderID(int orderID);
    List<TrainerOrder> findByFarmerProduct_ProductID(int productID);
    List<TrainerOrder> findByUser_UserID(int userID);
    List<TrainerOrder> findByFarmerProduct_User_UserID(int userID);
}
