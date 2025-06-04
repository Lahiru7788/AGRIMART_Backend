package com.example.AGRIMART.Entity.TrainerEntity;

import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trainerCourseChapters")
public class TrainerCourseChapters {

    @Id
    @Column(name = "chapter_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int chapterID;

    @Column(name = "chapter_name")
    private String chapterName;

    @Column(name = "chapter_no")
    private int chapterNo;

    @Column(name = "chapter_description")
    private String chapterDescription;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private TrainerCourse trainerCourse;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
