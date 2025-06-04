package com.example.AGRIMART.Repository.ConsumerRepository;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerCourseOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsumerCourseOrderRepository extends JpaRepository<ConsumerCourseOrder,Integer> {
    Optional<ConsumerCourseOrder> findByCourseName(String CourseName);
    ConsumerCourseOrder findByOrderID(int orderID);
    List<ConsumerCourseOrder> findByTrainerCourse_CourseID(int courseID);
    List<ConsumerCourseOrder> findByUser_UserID(int userID);
    List<ConsumerCourseOrder> findByTrainerCourse_User_UserID(int userID);
}
