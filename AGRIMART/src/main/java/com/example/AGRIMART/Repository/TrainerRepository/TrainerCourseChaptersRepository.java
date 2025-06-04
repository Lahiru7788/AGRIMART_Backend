package com.example.AGRIMART.Repository.TrainerRepository;

import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourseChapters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerCourseChaptersRepository extends JpaRepository<TrainerCourseChapters, Integer> {
    List<TrainerCourseChapters> findByTrainerCourse_CourseID(int courseID);
    TrainerCourseChapters findByChapterID(int chapterID);
}