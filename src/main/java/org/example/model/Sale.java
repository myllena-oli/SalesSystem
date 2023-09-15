package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sale")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id_seller", referencedColumnName = "id_seller")
    private Seller seller;
    @ManyToOne
    @JoinColumn(name = "id_customer", referencedColumnName = "id_customer")
    private Customer customer;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer productQuantity;
    private Double totalValue;

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
        updateTotalValue();
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
        updateTotalValue();
    }

    private void updateTotalValue() {
        if (productQuantity != null && productPrice != null) {
            this.totalValue = productQuantity * productPrice;
        } else {
            this.totalValue = null;
        }
    }
}

