package com.example.project.ecomm.ECommerce.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataFieldKey implements Serializable
{
    private Integer categoryMetadataFieldId;
    private Integer categoryId;
}
