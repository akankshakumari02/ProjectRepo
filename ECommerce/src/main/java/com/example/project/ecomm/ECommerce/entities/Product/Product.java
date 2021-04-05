package com.example.project.ecomm.ECommerce.entities.Product;


import com.example.project.ecomm.ECommerce.entities.User.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productName;
    private String productDescription;
    private String brand;
    private boolean Is_Cancellable;
    private boolean Is_Returnable;
    private boolean Is_Active;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductReview> productReviewList;

    @OneToMany(mappedBy = "product")
    private List<ProductVariation> productVariationList;

    /*@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
*/
}

