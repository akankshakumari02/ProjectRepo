/*
package com.example.project.ecomm.ECommerce.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataFieldValues
{
    @EmbeddedId
    private CategoryMetadataFieldKey categoryMetadataFieldKey = new CategoryMetadataFieldKey();

    private String value;

    @ManyToOne
    @MapsId("categoryMetadataFieldId")
    @JoinColumn(name = "category_metadata_field_id", referencedColumnName = "id")
    private CategoryMetadataField categorymetadatafield;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;


}
*/
