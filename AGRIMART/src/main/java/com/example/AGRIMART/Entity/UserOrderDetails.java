package com.example.AGRIMART.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userOrderDetails")
public class UserOrderDetails {

    @Id
    @Column(name = "userOrderDetails_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userOrderDetailsID;

    @Column(name = "order_number")
    private int orderNumber;

    @Column(name="added_date")
    private Date addedDate;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name ="quantity")
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

}
