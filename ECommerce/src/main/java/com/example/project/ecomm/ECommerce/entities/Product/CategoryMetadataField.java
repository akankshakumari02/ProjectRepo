/*
package com.example.project.ecomm.ECommerce.entities.Product;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonFilter("BeanFilter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "categoryMetadataField", cascade = CascadeType.ALL)
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues = new ArrayList<>();

    public CategoryMetadataField(String name)
    {
        this.name = name;
    }
}
*/
