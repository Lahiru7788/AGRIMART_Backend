package com.example.AGRIMART.Entity.SeedsAndFertilizerEntity;

import com.example.AGRIMART.Dto.FarmerDto.FarmerProductDto;
import com.example.AGRIMART.Dto.SeedsAndFetilizerDto.SeedsAndFetilizerProductDto;
import com.example.AGRIMART.Dto.UserCategoriesDto;
import com.example.AGRIMART.Entity.Credentials;
import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sfproducts")
public class SeedsAndFertilizerProduct {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productID;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_category")
    @Enumerated(EnumType.STRING)
    private SeedsAndFetilizerProductDto.ProductCategory productCategory;

    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage;

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

    @OneToOne(mappedBy = "seedsAndFertilizerProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SandFProductImage sandFProductImage;
}
