package com.example.AGRIMART.Entity.ConsumerEntity;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerCourseOrderDto;
import com.example.AGRIMART.Dto.ConsumerDto.ConsumerOrderDto;
import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerCourse;
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
@Table(name = "consumerCourseOrders")
public class ConsumerCourseOrder {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;

    @Column(name = "coursename")
    private String courseName;

    @Column(name = "course_category")
    @Enumerated(EnumType.STRING)
    private ConsumerCourseOrderDto.CourseCategory courseCategory;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description")
    private String description;

    @Column(name="added_date")
    private Date addedDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_addedToCart")
    private boolean isAddedToCart;

    @Column(name = "is_removedFromCart")
    private boolean isRemovedFromCart;

    @Column(name = "is_paid")
    private boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private TrainerCourse trainerCourse;


//    @OneToMany(mappedBy = "consumerOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ConsumerOrderImage> consumerOrderImages = new ArrayList<>();
}
