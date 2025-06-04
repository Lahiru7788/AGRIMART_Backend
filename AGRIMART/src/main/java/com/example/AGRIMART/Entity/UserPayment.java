package com.example.AGRIMART.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userPayment")
public class UserPayment {

    @Id
    @Column(name = "userpayment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userPaymentID;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "card_number")
    private long cardNumber;

    @Column(name = "cvc_number")
    private int cvcNumber;

    @Column(name="expiry_month")
    private int expiryMonth;

    @Column(name="expiry_year")
    private int expiryYear;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
