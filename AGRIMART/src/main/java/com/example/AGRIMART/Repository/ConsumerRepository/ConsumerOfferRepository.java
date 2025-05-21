package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOffer;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsumerOfferRepository extends JpaRepository<ConsumerOffer, Integer> {
    List<ConsumerOffer> findByConsumerAddOrder_OrderID(int orderID);
}
