package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto
{
    private Integer id;

    @Column(unique = true)
    private String name;

    private String description;

    private boolean Is_Cancellable = false;

    private boolean Is_Returnable = false;

    private boolean Is_Active = false;

    @Column(unique = true)
    private String brand;

    private Integer categoryId;

    private Integer sellerId;

}
