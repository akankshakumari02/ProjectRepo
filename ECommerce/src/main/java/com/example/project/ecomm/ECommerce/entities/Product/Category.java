/*
package com.example.project.ecomm.ECommerce.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Product> productList;

    @ManyToOne
    @JoinColumn(name = "parent_category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "category")
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValuesList;
} */

