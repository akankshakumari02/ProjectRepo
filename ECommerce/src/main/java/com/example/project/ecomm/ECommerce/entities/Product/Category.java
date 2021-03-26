package com.example.project.ecomm.ECommerce.entities.Product;

import javax.persistence.*;
import java.util.List;

@Entity
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CategoryMetadataFieldValues> getCategoryMetadataFieldValuesList() {
        return categoryMetadataFieldValuesList;
    }

    public void setCategoryMetadataFieldValuesList(List<CategoryMetadataFieldValues> categoryMetadataFieldValuesList) {
        this.categoryMetadataFieldValuesList = categoryMetadataFieldValuesList;
    }
}
