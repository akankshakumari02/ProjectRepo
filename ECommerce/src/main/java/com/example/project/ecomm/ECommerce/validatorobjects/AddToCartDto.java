package com.example.project.ecomm.ECommerce.validatorobjects;

import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.example.project.ecomm.ECommerce.entities.Product.ProductVariation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartDto //will use for displaying values
{
    private Integer id;

    private Integer userId;

    @NotNull
    private Integer quantity;

    @NotNull
    private ProductVariation productVariationId;
}
