package com.example.AGRIMART.Repository.SupermarketRepository;

import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketSeedsOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupermarketSeedsOrderRepository extends JpaRepository<SupermarketSeedsOrder,Integer> {

    Optional<SupermarketSeedsOrder> findByProductName(String ProductName);
    SupermarketSeedsOrder findByOrderID(int orderID);
    List<SupermarketSeedsOrder> findBySFProduct_ProductID(int productID);
    List<SupermarketSeedsOrder> findByUser_UserID(int userID);
    //    List<ConsumerSeedsOrder> findBySFProduct_User_UserID(int userID);
    List<SupermarketSeedsOrder> findBySFProduct_User_UserID(int userID);
}
