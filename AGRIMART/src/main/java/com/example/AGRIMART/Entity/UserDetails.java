package com.example.AGRIMART.Entity;

import com.example.AGRIMART.Dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userDetails")
public class UserDetails {
    @Id
    @Column(name = "userdetails_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userDetailsID;

    @Column(name = "userfirst_name")
    private String userFirstName;

    @Column(name = "userlast_name")
    private String userLastName;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

}
