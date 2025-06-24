package com.example.AGRIMART.Entity.TrainerEntity;

import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerCourseOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerCourseOrder;
import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trainerCourses")
public class TrainerCourse {

    @Id
    @Column(name = "course_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer courseID;

    @Column(name = "coursename")
    private String courseName;

    @Column(name = "course_category", length = 100)
    @Enumerated(EnumType.STRING)
    private TrainerAddCourseDto.CourseCategory courseCategory;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name ="drive_link", length = 1000)
    private String driveLink;

    @Column(name = "youtube_link", length = 1000)
    private String youtubeLink;

    @Column(name = "description")
    private String description;

    @Column(name="added_date")
    private Date addedDate;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_addedToCart")
    private boolean isAddedToCart;

    @Column(name = "is_removedFromCart")
    private boolean isRemovedFromCart;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainerCourseImage> trainerCourseImages = new ArrayList<>();

    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainerCourseChapters> trainerCourseChapters = new ArrayList<>();

    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsumerCourseOrder> consumerCourseOrders = new ArrayList<>();

    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FarmerCourseOrder> farmerCourseOrders = new ArrayList<>();

    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainerCourseOffer> trainerCourseOffers = new ArrayList<>();
//
//    @OneToMany(mappedBy = "SFProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<SupermarketSeedsOrder> supermarketSeedsOrders = new ArrayList<>();
//
//    @OneToMany(mappedBy = "SFProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<FarmerSeedsOrder> farmerSeedsOrders = new ArrayList<>();
}
