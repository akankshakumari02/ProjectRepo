package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto
{
    private Integer id;

    @Column(unique = true) //adding unique values
    private String name;

    private Integer parentId;
}
