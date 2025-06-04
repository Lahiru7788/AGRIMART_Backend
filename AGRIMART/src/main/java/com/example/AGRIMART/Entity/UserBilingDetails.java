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
@Table(name = "userBillingDetails")
public class UserBilingDetails {

    @Id
    @Column(name = "userbillingdetails_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userBillingDetailsID;

    @Column(name = "userfirst_name")
    private String userFirstName;

    @Column(name = "userlast_name")
    private String userLastName;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "address")
    private String address;

    @Column(name="added_date")
    private Date addedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
