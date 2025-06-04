package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerAddCourseRepository extends JpaRepository<TrainerCourse,Integer> {
    Optional<TrainerCourse> findByCourseName(String CourseName);
//    TrainerCourse findByCourseID(int courseID);
    Optional<TrainerCourse> findByCourseID(int courseID);
    List<TrainerCourse> findByUser_UserID(int userID);
}