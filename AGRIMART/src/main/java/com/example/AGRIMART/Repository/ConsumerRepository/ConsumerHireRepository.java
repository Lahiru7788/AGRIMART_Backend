package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerHire;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumerHireRepository extends JpaRepository<ConsumerHire,Integer> {

    Optional<ConsumerHire> findByName(String Name);
    ConsumerHire findByOrderID(int orderID);
    List<ConsumerHire> findByTrainerHiring_HireID(int hireID);
    List<ConsumerHire> findByUser_UserID(int userID);
    List<ConsumerHire> findByTrainerHiring_User_UserID(int userID);
}
