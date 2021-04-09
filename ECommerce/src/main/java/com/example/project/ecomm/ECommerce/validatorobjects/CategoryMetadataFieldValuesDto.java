package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataFieldValuesDto
{
    private Integer categoryMetadataFieldId;
    private Integer categoryId;
    private String name; //valueName
}
