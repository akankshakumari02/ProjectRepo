package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProductVariationDto
{
    private Integer productVariationId;
    private int price;
    private List<MetadataDto> updateMetadata;
}
