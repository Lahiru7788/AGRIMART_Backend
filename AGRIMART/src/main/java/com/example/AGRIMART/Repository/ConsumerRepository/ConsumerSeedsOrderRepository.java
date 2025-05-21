package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumerSeedsOrderRepository extends JpaRepository<ConsumerSeedsOrder,Integer> {
    Optional<ConsumerSeedsOrder> findByProductName(String ProductName);
    ConsumerSeedsOrder findByOrderID(int orderID);
    List<ConsumerSeedsOrder> findBySFProduct_ProductID(int productID);
    List<ConsumerSeedsOrder> findByUser_UserID(int userID);
//    List<ConsumerSeedsOrder> findBySFProduct_User_UserID(int userID);
    List<ConsumerSeedsOrder> findBySFProduct_User_UserID(int userID);
//    List<ConsumerOrder> findByUser_UserID(int userID);

}
