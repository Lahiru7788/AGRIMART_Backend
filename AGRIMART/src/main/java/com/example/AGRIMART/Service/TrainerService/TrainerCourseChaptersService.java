package com.example.AGRIMART.Service.TrainerService;

import com.example.AGRIMART.Dto.TrainerDto.TrainerCourseChaptersDto;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersAddResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersDeleteResponse;
import com.example.AGRIMART.Dto.response.TrainerResponse.TrainerCourseChaptersGetResponse;

public interface TrainerCourseChaptersService {

    TrainerCourseChaptersAddResponse saveOrUpdate(TrainerCourseChaptersDto trainerCourseChaptersDto);
    TrainerCourseChaptersGetResponse getTrainerCourseChaptersByCourseID(int courseID);
    TrainerCourseChaptersDeleteResponse DeleteTrainerCourseChaptersResponse(int chapterID);
}
