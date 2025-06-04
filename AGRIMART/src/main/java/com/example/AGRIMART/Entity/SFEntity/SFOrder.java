package com.example.AGRIMART.Entity.SFEntity;

import com.example.AGRIMART.Dto.SFDto.SFOrderDto;
import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
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
@Table(name = "sfOrders")
public class SFOrder {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private SFOrderDto.ProductCategory productCategory;

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
    private FarmerProduct farmerProduct;

    @OneToMany(mappedBy = "SFOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SFOrderImage> sfOrderImages = new ArrayList<>();
}
