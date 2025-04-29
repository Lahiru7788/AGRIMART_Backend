package com.example.AGRIMART.Entity.FarmerEntity;

import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "farmerProductImage")
public class FarmerProductImage {

    @Id
    @Column(name = "Image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int imageID;

    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage;

//    @OneToOne
//    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
//    private FarmerProduct farmerProduct;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private FarmerProduct farmerProduct;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
