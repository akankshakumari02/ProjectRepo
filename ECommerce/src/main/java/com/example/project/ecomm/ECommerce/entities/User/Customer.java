package com.example.project.ecomm.ECommerce.entities.User;
import com.example.project.ecomm.ECommerce.entities.Order.Cart;
import com.example.project.ecomm.ECommerce.entities.Order.Order;
import com.example.project.ecomm.ECommerce.entities.Product.ProductReview;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@JsonFilter("Filter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Customer extends User
{
    @ElementCollection
    @CollectionTable(name = "customer_contacts", joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"))
    //@NotNull
    private List<Long> contactList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ProductReview> productReviewList;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Order> orderList;

    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL)
    private List<Cart> carts;
}
