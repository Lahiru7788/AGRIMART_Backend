package com.example.AGRIMART.Entity.SupermarketEntity;

import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supermarketSeedsOrderProductImage")
public class SupermarketSeedsOrderImage {
    @Id
    @Column(name = "Image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int imageID;

    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private SupermarketSeedsOrder supermarketSeedsOrder;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
