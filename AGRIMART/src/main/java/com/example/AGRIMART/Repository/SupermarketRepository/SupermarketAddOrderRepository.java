package com.example.AGRIMART.Repository.SupermarketRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupermarketAddOrderRepository extends JpaRepository<SupermarketAddOrder,Integer> {
    Optional<SupermarketAddOrder> findByProductName(String ProductName);
    SupermarketAddOrder findByOrderID(int orderID);
    List<SupermarketAddOrder> findByUser_UserID(int userID);
}
