package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.UserOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOrderDetailsRepository extends JpaRepository<UserOrderDetails,Integer> {
    List<UserOrderDetails> findByUser_UserID(int userID);
    List<UserOrderDetails> findByUserOrderDetailsID(int userOrderDetailsID);
}