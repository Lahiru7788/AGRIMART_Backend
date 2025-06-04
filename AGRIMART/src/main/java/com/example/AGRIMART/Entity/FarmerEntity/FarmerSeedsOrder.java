package com.example.AGRIMART.Entity.FarmerEntity;

import com.example.AGRIMART.Dto.ConsumerDto.ConsumerSeedsOrderDto;
import com.example.AGRIMART.Dto.FarmerDto.FarmerSeedsOrderDto;
import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrderImage;
import com.example.AGRIMART.Entity.SFEntity.SFProduct;
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
@Table(name = "farmerSeedsOrders")
public class FarmerSeedsOrder {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private FarmerSeedsOrderDto.ProductCategory productCategory;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name ="required_quantity")
    private double requiredQuantity;

    @Column(name = "description")
    private String description;

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
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private SFProduct SFProduct;

    @OneToMany(mappedBy = "farmerSeedsOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FarmerSeedsOrderImage> farmerSeedsOrderImages = new ArrayList<>();
}
