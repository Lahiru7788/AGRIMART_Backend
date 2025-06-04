package com.example.AGRIMART.Entity.FarmerEntity;

import com.example.AGRIMART.Entity.ConsumerEntity.ConsumerSeedsOrder;
import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "farmerSeedsOrderProductImage")
public class FarmerSeedsOrderImage {

    @Id
    @Column(name = "Image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int imageID;

    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private FarmerSeedsOrder farmerSeedsOrder;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
