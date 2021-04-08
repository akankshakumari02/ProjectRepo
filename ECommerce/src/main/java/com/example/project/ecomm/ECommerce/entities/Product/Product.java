package com.example.project.ecomm.ECommerce.entities.Product;


import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonFilter("Filter")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String brand;
    private boolean Is_Cancellable;
    private boolean Is_Returnable;
    private boolean Is_Active = false;
    private boolean Is_Deleted = false;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> productReviewList;

    @OneToMany(mappedBy = "product")
    private List<ProductVariation> productVariationList;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isIs_Cancellable() {
        return Is_Cancellable;
    }

    public void setIs_Cancellable(boolean is_Cancellable) {
        Is_Cancellable = is_Cancellable;
    }

    public boolean isIs_Returnable() {
        return Is_Returnable;
    }

    public void setIs_Returnable(boolean is_Returnable) {
        Is_Returnable = is_Returnable;
    }

    public boolean isIs_Active() {
        return Is_Active;
    }

    public void setIs_Active(boolean is_Active) {
        Is_Active = is_Active;
    }

    public boolean isIs_Deleted() {
        return Is_Deleted;
    }

    public void setIs_Deleted(boolean is_Deleted) {
        Is_Deleted = is_Deleted;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public List<ProductReview> getProductReviewList() {
        return productReviewList;
    }

    public void setProductReviewList(List<ProductReview> productReviewList) {
        this.productReviewList = productReviewList;
    }

    public List<ProductVariation> getProductVariationList() {
        return productVariationList;
    }

    public void setProductVariationList(List<ProductVariation> productVariationList) {
        this.productVariationList = productVariationList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

