package com.example.AGRIMART.Repository.FarmerRepositoty;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerCourseOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerCourseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FarmerCourseOrderRepository extends JpaRepository<FarmerCourseOrder,Integer> {
    Optional<FarmerCourseOrder> findByCourseName(String CourseName);
    FarmerCourseOrder findByOrderID(int orderID);
    List<FarmerCourseOrder> findByTrainerCourse_CourseID(int courseID);
    List<FarmerCourseOrder> findByUser_UserID(int userID);
    List<FarmerCourseOrder> findByTrainerCourse_User_UserID(int userID);
}