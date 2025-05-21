package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerPreOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumerAddOrderRepository extends JpaRepository<ConsumerAddOrder,Integer> {
    Optional<ConsumerAddOrder> findByProductName(String ProductName);
    ConsumerAddOrder findByOrderID(int orderID);
    List<ConsumerAddOrder> findByUser_UserID(int userID);
}
