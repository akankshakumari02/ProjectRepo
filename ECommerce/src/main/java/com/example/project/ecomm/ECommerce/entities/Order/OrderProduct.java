package com.example.project.ecomm.ECommerce.entities.Order;

import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonFilter("OrderProductFilter")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderProduct
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;
    private int price;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String productVariationMetadata; //metadata

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;


    @OneToOne(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private OrderStatus orderStatus;

}
