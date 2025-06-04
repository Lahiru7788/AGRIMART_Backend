package com.example.AGRIMART.Entity.FarmerEntity;

import com.example.AGRIMART.Entity.TrainerEntity.TrainerHiring;
import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "farmerHire")
public class FarmerHire {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;

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

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    @Column(name = "is_rejected")
    private boolean isRejected;

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
    @JoinColumn(name = "hire_id", referencedColumnName = "hire_id")
    private TrainerHiring trainerHiring;
}
