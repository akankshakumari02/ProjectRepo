package com.example.project.ecomm.ECommerce.entities.Order;

import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.FromStatus;
import com.example.project.ecomm.ECommerce.entities.Order.OrderStatusEnums.ToStatus;
import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatus
{
    @Id
    private int id;

    private String transitionNotesComments;
    private FromStatus fromStatus;
    private ToStatus toStatus;

    //unidirectional
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;
}
