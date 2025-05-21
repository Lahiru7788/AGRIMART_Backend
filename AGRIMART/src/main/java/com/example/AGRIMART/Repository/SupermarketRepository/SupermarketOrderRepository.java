package com.example.AGRIMART.Repository.SupermarketRepository;

import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupermarketOrderRepository extends JpaRepository<SupermarketOrder,Integer> {

    Optional<SupermarketOrder> findByProductName(String ProductName);
    SupermarketOrder findByOrderID(int orderID);
    List<SupermarketOrder> findByFarmerProduct_ProductID(int productID);
    List<SupermarketOrder> findByUser_UserID(int userID);
    List<SupermarketOrder> findByFarmerProduct_User_UserID(int userID);
}
