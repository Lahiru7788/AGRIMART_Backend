package com.example.AGRIMART.Entity.ConsumerEntity;

import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addOrderProductImage")
public class CAddOrderImage {

    @Id
    @Column(name = "Image_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int imageID;

    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    private byte[] productImage;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private ConsumerAddOrder consumerAddOrder;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

}
