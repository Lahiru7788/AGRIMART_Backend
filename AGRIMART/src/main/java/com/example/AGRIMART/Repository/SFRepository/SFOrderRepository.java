package com.example.AGRIMART.Repository.SFRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.SFEntity.SFOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SFOrderRepository extends JpaRepository<SFOrder,Integer> {
    Optional<SFOrder> findByProductName(String ProductName);
    SFOrder findByOrderID(int orderID);
    List<SFOrder> findByFarmerProduct_ProductID(int productID);
    List<SFOrder> findByUser_UserID(int userID);
    List<SFOrder> findByFarmerProduct_User_UserID(int userID);
//    List<ConsumerOrder> findByUser_UserID(int userID);

}
