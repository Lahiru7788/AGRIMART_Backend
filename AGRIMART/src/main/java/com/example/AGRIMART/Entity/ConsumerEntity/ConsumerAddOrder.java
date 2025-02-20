package com.example.AGRIMART.Entity.ConsumerEntity;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerAddOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
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
@Table(name = "consumerAddOrders")
public class ConsumerAddOrder {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;

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

    @OneToMany(mappedBy = "consumerAddOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CAddOrderImage> cAddOrderImages = new ArrayList<>();

    @OneToMany(mappedBy = "consumerAddOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsumerOffer> consumerOffer = new ArrayList<>();
}
