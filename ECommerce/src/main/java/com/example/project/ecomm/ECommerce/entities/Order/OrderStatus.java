package com.example.project.ecomm.ECommerce.entities.Order;

import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.FromStatus;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.ToStatus;
import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonFilter("Filter")
public class OrderStatus
{
    @Id
    private int id;

    private String transitionNotesComments;
    private String fromStatus;
    private String toStatus;

    //unidirectional
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;
}
