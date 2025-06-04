package com.example.AGRIMART.Entity.TrainerEntity;

import com.example.AGRIMART.Dto.TrainerDto.TrainerAddCourseDto;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerCourseOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerHire;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerCourseOrder;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerHire;
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
@Table(name = "trainerHiring")
public class TrainerHiring {

    @Id
    @Column(name = "hire_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer hireID;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "qualifications")
    private String qualifications;

    @Column(name = "years_of_experience")
    private int yearsOfExperience;

    @Column(name="added_date")
    private Date addedDate;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "trainerHiring", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainerHiringOffer> trainerHiringOffers = new ArrayList<>();

    @OneToMany(mappedBy = "trainerHiring", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FarmerHire> farmerHires = new ArrayList<>();

    @OneToMany(mappedBy = "trainerHiring", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsumerHire> consumerHires = new ArrayList<>();

//    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<TrainerCourseChapters> trainerCourseChapters = new ArrayList<>();
//
//    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ConsumerCourseOrder> consumerCourseOrders = new ArrayList<>();
//
//    @OneToMany(mappedBy = "trainerCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<FarmerCourseOrder> farmerCourseOrders = new ArrayList<>();
}
