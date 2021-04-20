package com.example.project.ecomm.ECommerce.entities.Order;

import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonFilter("Filter")
public class Cart
{
    @EmbeddedId
    private CustomerProductVariationKey customerProductVariationKey = new CustomerProductVariationKey();
    private int quantity;

  private boolean isWishlistItem;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @MapsId("productVariationId")
    @JoinColumn(name = "product_variation_id")
    private ProductVariation productVariation;


   /* @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date modifiedDate;*/
}
