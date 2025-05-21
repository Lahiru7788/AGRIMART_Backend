package com.example.AGRIMART.Repository.SupermarketRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOffer;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupermarketOfferRepository extends JpaRepository<SupermarketOffer, Integer> {
    List<SupermarketOffer> findBySupermarketAddOrder_OrderID(int orderID);
}
