package com.example.AGRIMART.Repository;

import com.example.AGRIMART.Entity.UserBilingDetails;
import com.example.AGRIMART.Entity.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPaymentRepository extends JpaRepository<UserPayment,Integer> {
    List<UserPayment> findByUser_UserID(int userID);
    List<UserPayment> findByUserPaymentID(int uerPaymentID);
}
