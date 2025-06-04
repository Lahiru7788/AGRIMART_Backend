package com.example.AGRIMART.Entity.SupermarketEntity;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Entity.ConsumerEntity.CAddOrderImage;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOffer;
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
@Table(name = "supermarketAddOrders")
public class SupermarketAddOrder {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private ConsumerAddOrderDto.ProductCategory productCategory;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name ="required_quantity")
    private double requiredQuantity;

    @Column(name = "required_time")
    private String requiredTime ;

    @Column(name = "description")
    private String description;

    @Column(name="added_date")
    private Date addedDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_confirmed")
    private boolean isConfirmed;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "supermarketAddOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SAddOrderImage> sAddOrderImages = new ArrayList<>();

    @OneToMany(mappedBy = "supermarketAddOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupermarketOffer> supermarketOffers = new ArrayList<>();

}
