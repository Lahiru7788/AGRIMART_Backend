package com.example.AGRIMART.Entity.TrainerEntity;

import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketAddOrder;
import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trainerCourseImage")
public class TrainerCourseImage {

    @Id
    @Column(name = "Image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int imageID;

    @Column(name = "course_image", columnDefinition = "LONGBLOB")
    private byte[] courseImage;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private TrainerCourse trainerCourse;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
