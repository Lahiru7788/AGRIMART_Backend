package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerCourseOfferRepository extends JpaRepository<TrainerCourseOffer, Integer> {
    List<TrainerCourseOffer> findByTrainerCourse_CourseID(int courseID);
    TrainerCourseOffer findByOfferID(int offerID);

}
