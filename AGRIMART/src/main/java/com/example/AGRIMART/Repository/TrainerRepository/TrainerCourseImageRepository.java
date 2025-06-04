package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerCourseImageRepository extends JpaRepository<TrainerCourseImage, Integer> {
    Optional<TrainerCourseImage> findByTrainerCourse_CourseID(int orderID);
}
