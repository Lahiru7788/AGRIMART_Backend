package com.example.AGRIMART.Entity.ConsumerEntity;

import com.example.AGRIMART.Entity.FarmerEntity.FarmerProduct;
import com.example.AGRIMART.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "consumerOffer")
public class ConsumerOffer {

    @Id
    @Column(name = "offer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int offerID;

    @Column(name = "offer_name")
    private String offerName;

    @Column(name = "new_price")
    private BigDecimal newPrice;

    @Column(name = "offer_description")
    private String offerDescription;

    @Column(name="added_date")
    private LocalDateTime addedDate;

    @PrePersist
    protected void onCreate() {
        addedDate = LocalDateTime.now();
    }

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private ConsumerAddOrder consumerAddOrder;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}



