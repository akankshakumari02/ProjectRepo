package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataFieldDto {
    @Column(unique = true)
    private String name;
}
