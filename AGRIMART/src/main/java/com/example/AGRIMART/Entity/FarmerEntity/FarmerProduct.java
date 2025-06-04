package com.example.AGRIMART.Entity.FarmerEntity;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.SFEntity.SFOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketOrder;
import com.example.AGRIMART.Entity.TrainerEntity.TrainerOrder;
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
@Table(name = "fproduct")
public class FarmerProduct {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer productID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private FarmerProductDto.ProductCategory productCategory;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name ="available_quantity")
    private double availableQuantity;

    @Column(name = "minimum_quantity")
    private double minimumQuantity;

    @Column(name = "description")
    private String description;

    @Column(name="added_date")
    private Date addedDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "is_quantityLowered")
    private boolean isQuantityLowered;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

//    @OneToOne(mappedBy = "farmerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private FarmerProductImage farmerProductImage;


    @OneToMany(mappedBy = "farmerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FarmerOffer> farmerOffer = new ArrayList<>();

    @OneToMany(mappedBy = "farmerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsumerOrder> consumerOrders = new ArrayList<>();

    @OneToMany(mappedBy = "farmerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupermarketOrder> supermarketOrders = new ArrayList<>();

    @OneToMany(mappedBy = "farmerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FarmerProductImage> farmerProductImage = new ArrayList<>();

    @OneToMany(mappedBy = "farmerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SFOrder> sfOrders = new ArrayList<>();

    @OneToMany(mappedBy = "farmerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainerOrder> trainerOrders = new ArrayList<>();
}
