package com.example.project.ecomm.ECommerce.validatorobjects;

import lombok.Data;
import java.util.List;

@Data
public class ProductVariationDto {

    private Integer id;
    private int quantityAvailable;
    private int price;
    private String image;
    private Integer productId;
    private List<MetadataDto> metadataDto;
    public void getMetadata() {
    }
}
