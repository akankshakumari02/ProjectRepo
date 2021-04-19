package com.example.project.ecomm.ECommerce.entities.Order;

import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;
    private int price;
    private String productVariationMetadata;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id")
    private ProductVariation productVariation;


    @OneToOne(mappedBy = "orderProduct")
    private OrderStatus orderStatus;

}
