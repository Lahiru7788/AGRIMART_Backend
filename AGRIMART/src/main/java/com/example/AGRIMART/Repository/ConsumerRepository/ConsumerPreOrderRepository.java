package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerPreOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsumerPreOrderRepository extends JpaRepository<ConsumerPreOrder,Integer> {
    Optional<ConsumerPreOrder> findByProductName(String ProductName);
}
