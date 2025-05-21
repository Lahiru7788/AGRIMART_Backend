package com.example.AGRIMART.Entity.SFEntity;

import com.example.AGRIMART.Dto.SFDto.SFProductDto;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerOrder;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import com.example.AGRIMART.Entity.SupermarketEntity.SupermarketSeedsOrder;
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
@Table(name = "sfproducts")
public class SFProduct {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private SFProductDto.ProductCategory productCategory;

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

    @OneToMany(mappedBy = "SFProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SFProductImage> SFProductImages = new ArrayList<>();

    @OneToMany(mappedBy = "SFProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SFOffer> sfOffers = new ArrayList<>();

    @OneToMany(mappedBy = "SFProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConsumerSeedsOrder> consumerSeedsOrders = new ArrayList<>();

    @OneToMany(mappedBy = "SFProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SupermarketSeedsOrder> supermarketSeedsOrders = new ArrayList<>();
}
