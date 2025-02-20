package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerAddOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerPreOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsumerAddOrderRepository extends JpaRepository<ConsumerAddOrder,Integer> {
    Optional<ConsumerAddOrder> findByProductName(String ProductName);
}
